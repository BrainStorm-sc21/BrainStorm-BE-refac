package com.brainstrom.meokjang.deal.service;

import com.brainstrom.meokjang.deal.domain.Deal;
import com.brainstrom.meokjang.deal.dto.request.DealRequest;
import com.brainstrom.meokjang.deal.dto.response.DealInfoResponse;
import com.brainstrom.meokjang.deal.repository.DealRepository;
import com.brainstrom.meokjang.user.domain.User;
import com.brainstrom.meokjang.user.repository.UserRepository;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.*;
import com.oracle.bmc.objectstorage.responses.GetBucketResponse;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class DealService {

    private final DealRepository dealRepository;
    private final UserRepository userRepository;
    private final ObjectStorage objectStorage;
    private final String ociNamespace;
    private final String ociBucketName;

    @Autowired
    public DealService(@Value("${OCI_CONFIG_PATH}") String ociConfigPath,
                    @Value("${OCI_NAMESPACE}") String ociNamespace,
                    @Value("${OCI_BUCKET_NAME}") String ociBucketName,
                    DealRepository dealRepository, UserRepository userRepository) throws IOException {
        try {
            ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(ociConfigPath, "DEFAULT");
            AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
            objectStorage = ObjectStorageClient.builder()
                    .region(Region.AP_CHUNCHEON_1)
                    .build(provider);

//            ------------------ OCI SDK TEST ------------------
            GetNamespaceResponse namespaceResponse = objectStorage.getNamespace(GetNamespaceRequest.builder().build());
            String namespaceName = namespaceResponse.getValue();
            System.out.println("namespaceName: " + namespaceName);
            List<GetBucketRequest.Fields> fieldsList = new ArrayList<>(2);
            fieldsList.add(GetBucketRequest.Fields.ApproximateCount);
            fieldsList.add(GetBucketRequest.Fields.ApproximateSize);

            GetBucketRequest getBucketRequest = GetBucketRequest.builder()
                    .namespaceName(ociNamespace)
                    .bucketName(ociBucketName)
                    .fields(fieldsList)
                    .build();
            System.out.println("Fetching Bucket Details");
            GetBucketResponse getBucketResponse = objectStorage.getBucket(getBucketRequest);
            System.out.println("Bucket Name: " + getBucketResponse.getBucket().getName());
            System.out.println("Bucket Compartment Id: " + getBucketResponse.getBucket().getCompartmentId());
            System.out.println("Bucket Size: " + getBucketResponse.getBucket().getApproximateSize());
            System.out.println("Bucket Object Count: " + getBucketResponse.getBucket().getApproximateCount());
//            ------------------ OCI SDK TEST ------------------

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        this.ociNamespace = ociNamespace;
        this.ociBucketName = ociBucketName;
        this.dealRepository = dealRepository;
        this.userRepository = userRepository;
    }

    public void save(DealRequest dealRequest) {
        try {
            User user = userRepository.findByUserId(dealRequest.getUserId())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            String[] imageList = uploadImage(dealRequest);
            Deal deal = Deal.builder()
                    .userId(dealRequest.getUserId())
                    .dealType(dealRequest.getDealType())
                    .dealName(dealRequest.getDealName())
                    .dealContent(dealRequest.getDealContent())
                    .location(user.getLocation())
                    .latitude(user.getLatitude())
                    .longitude(user.getLongitude())
                    .image1(imageList[0] != null ? imageList[0] : null)
                    .image2(imageList[1] != null ? imageList[1] : null)
                    .image3(imageList[2] != null ? imageList[2] : null)
                    .image4(imageList[3] != null ? imageList[3] : null)
                    .isDeleted(false)
                    .build();
            dealRepository.save(deal);
        } catch (IllegalStateException | IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private String[] uploadImage(DealRequest dealRequest) throws IOException {
        String[] imageList = new String[4];
        if (dealRequest.getImage1() != null) {
            imageList[0] = upload(dealRequest.getImage1());
        }
        if (dealRequest.getImage2() != null) {
            imageList[1] = upload(dealRequest.getImage2());
        }
        if (dealRequest.getImage3() != null) {
            imageList[2] = upload(dealRequest.getImage3());
        }
        if (dealRequest.getImage4() != null) {
            imageList[3] = upload(dealRequest.getImage4());
        }
        return imageList;
    }

    private String upload(MultipartFile image) throws IOException {
        UploadConfiguration uploadConfiguration =
                UploadConfiguration.builder()
                .allowMultipartUploads(true)
                .allowParallelUploads(true)
                .build();
        UploadManager uploadManager = new UploadManager(objectStorage, uploadConfiguration);
        String fileName = UUID.randomUUID().toString();
        PutObjectRequest por = PutObjectRequest.builder()
                .bucketName(ociBucketName)
                .namespaceName(ociNamespace)
                .objectName(fileName)
                .contentType(image.getContentType())
                .opcMeta(null)
                .build();
        File file = convertMultipartFileToFile(image);
        if (file == null) {
            return null;
        }
        image.transferTo(file);
        UploadManager.UploadRequest uploadDetails =
                UploadManager.UploadRequest.builder(file)
                .allowOverwrite(true)
                .build(por);
        UploadManager.UploadResponse uploadResponse = uploadManager.upload(uploadDetails);
        System.out.println("uploadResponse: " + uploadResponse);
        file.delete();
        return fileName;
    }

    private File convertMultipartFileToFile(MultipartFile image) {
        if (image.isEmpty()) {
            return null;
        }
        File file = new File(Objects.requireNonNull(image.getOriginalFilename()));
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(image.getBytes());
            fos.close();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
        return file;
    }

    public List<DealInfoResponse> aroundDealList(Long userId) {
        try {
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            List<Deal> deals = dealRepository.findAroundDealList(user.getLatitude(), user.getLongitude());
            List<DealInfoResponse> dealLists = new ArrayList<>();
            for (Deal d : deals) {
                if (d.getIsDeleted()) {
                    continue;
                }
                Double distance = getDistance(user.getLatitude(), user.getLongitude(), d.getLatitude(), d.getLongitude());
                DealInfoResponse res = DealInfoResponse.builder()
                        .dealId(d.getDealId())
                        .userId(d.getUserId())
                        .dealType(d.getDealType())
                        .dealName(d.getDealName())
                        .dealContent(d.getDealContent())
                        .latitude(d.getLatitude())
                        .longitude(d.getLongitude())
                        .distance(distance)
                        .image1(d.getImage1())
                        .image2(d.getImage2())
                        .image3(d.getImage3())
                        .image4(d.getImage4())
                        .isClosed(d.getIsClosed())
                        .createdAt(d.getCreatedAt())
                        .build();
                dealLists.add(res);
            }
            return dealLists;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public DealInfoResponse getDealInfo(Long dealId) {
        try {
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            return DealInfoResponse.builder()
                    .dealId(deal.getDealId())
                    .userId(deal.getUserId())
                    .dealType(deal.getDealType())
                    .dealName(deal.getDealName())
                    .dealContent(deal.getDealContent())
                    .latitude(deal.getLatitude())
                    .longitude(deal.getLongitude())
                    .distance(null)
                    .image1(deal.getImage1())
                    .image2(deal.getImage2())
                    .image3(deal.getImage3())
                    .image4(deal.getImage4())
                    .isClosed(deal.getIsClosed())
                    .createdAt(deal.getCreatedAt())
                    .build();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void updateDealInfo(Long dealId, DealRequest dealRequest) {
        try {
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            String[] imageList = uploadImage(dealRequest);
            deal.update(dealRequest.getDealType(), dealRequest.getDealName(), dealRequest.getDealContent(), imageList);
        } catch (IllegalStateException | IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void deleteDeal(Long dealId) {
        try {
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            String[] imageList = deal.getImageList();
            for (String image : imageList) {
                if (image != null) {
                    DeleteObjectRequest dor = DeleteObjectRequest.builder()
                            .bucketName(ociBucketName)
                            .namespaceName(ociNamespace)
                            .objectName(image)
                            .build();
                    objectStorage.deleteObject(dor);
                }
            }
            deal.delete();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private Double getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;
    }

    public List<DealInfoResponse> myDealList(Long userId) {
        try {
            List<Deal> deals = dealRepository.findByUserId(userId);
            List<DealInfoResponse> dealLists = new ArrayList<>();
            for (Deal d : deals) {
                if (d.getIsDeleted()) {
                    continue;
                }
                DealInfoResponse res = DealInfoResponse.builder()
                        .dealId(d.getDealId())
                        .userId(d.getUserId())
                        .dealType(d.getDealType())
                        .dealName(d.getDealName())
                        .dealContent(d.getDealContent())
                        .latitude(d.getLatitude())
                        .longitude(d.getLongitude())
                        .distance((double) 0)
                        .image1(d.getImage1())
                        .image2(d.getImage2())
                        .image3(d.getImage3())
                        .image4(d.getImage4())
                        .isClosed(d.getIsClosed())
                        .createdAt(d.getCreatedAt())
                        .build();
                dealLists.add(res);
            }
            return dealLists;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void completeDeal(Long dealId) {
        try {
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            deal.complete();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
