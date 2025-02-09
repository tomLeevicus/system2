import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "资产基本信息实体")
public class AssetBaseInfo {
    @Schema(description = "资产ID", example = "1001")
    private Long id;
    
    @Schema(description = "资产分类ID", example = "300")
    private Long classificationId;
    
    @Schema(description = "资产状态（0-闲置，1-使用中）", example = "0")
    private Integer status;
    
    @Schema(description = "采购价格（元）", example = "5000.00")
    private BigDecimal purchasePrice;
    
    @Schema(description = "资产编码", example = "ASSET-2023-001")
    private String assetCode;
    
    @Schema(description = "资产型号", example = "ThinkPad X1 Carbon")
    private String model;
    
    @Schema(description = "生产厂商", example = "联想集团")
    private String manufacturer;
    
    @Schema(description = "采购日期", example = "2023-01-15")
    private LocalDate purchaseDate;
} 