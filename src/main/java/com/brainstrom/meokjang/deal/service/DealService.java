package com.brainstrom.meokjang.deal.service;

import com.brainstrom.meokjang.deal.domain.Deal;
import com.brainstrom.meokjang.deal.dto.request.DealRequest;
import com.brainstrom.meokjang.deal.dto.response.DealInfoResponse;
import com.brainstrom.meokjang.deal.repository.DealRepository;
import com.brainstrom.meokjang.user.domain.User;
import com.brainstrom.meokjang.user.repository.UserRepository;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DealService {

    private final DealRepository dealRepository;
    private final UserRepository userRepository;
    private final ObjectStorage objectStorage;
    private final String ociConfigPath;
    private final String ociProfile;
    private final String ociNamespace;
    private final String ociBucketName;

    @Autowired
    public DealService(@Value("@{OCI_CONFIG_PATH}") String ociConfigPath,
                    @Value("@{OCI_PROFILE}") String ociProfile,
                    @Value("@{OCI_NAMESPACE}") String ociNamespace,
                    @Value("@{OCI_BUCKET_NAME}") String ociBucketName,
                    DealRepository dealRepository, UserRepository userRepository) throws IOException {
        this.ociConfigPath = ociConfigPath;
        this.ociProfile = ociProfile;
        try {
            ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(ociConfigPath, ociProfile);
            ConfigFileAuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
            objectStorage = ObjectStorageClient.builder().region("ap-seoul-1").build(provider);
        } catch (IOException e) {
            throw new IOException("OCI 설정 파일을 찾을 수 없습니다.");
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
                    .image1(imageList[0])
                    .image2(imageList[1])
                    .image3(imageList[2])
                    .image4(imageList[3])
                    .build();
            dealRepository.save(deal);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private String[] uploadImage(DealRequest dealRequest) {
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

    private String upload(MultipartFile image) {
        String fileName = image.getOriginalFilename();
        PutObjectRequest por = PutObjectRequest.builder()
                .bucketName(ociBucketName)
                .namespaceName(ociNamespace)
                .objectName(fileName)
                .contentType(image.getContentType())
                .build();
        objectStorage.putObject(por);
        return fileName;
    }

    public List<DealInfoResponse> aroundDealList(Long userId) {
        try {
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            List<Deal> deals = dealRepository.findAroundDealList(user.getLatitude(), user.getLongitude());
            List<DealInfoResponse> dealLists = new ArrayList<>();
            for (Deal d : deals) {
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
            deal.update(dealRequest);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void deleteDeal(Long dealId) {
        try {
            dealRepository.deleteById(dealId);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private Double getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371;
        Double dLat = Math.toRadians(lat2 - lat1);
        Double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = R * c * 1000; // 결과 단위 미터(m)
        return distance;
    }
}
