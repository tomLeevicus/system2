package com.project.system2.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 固定资产折旧计算工具类
 */
public final class DepreciationCalculator {

    private static final int DEFAULT_SCALE = 2; // 默认保留小数位数
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP; // 默认舍入模式

    // 私有构造函数，防止实例化
    private DepreciationCalculator() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 计算年限平均法（直线法）的年折旧额
     * 公式：年折旧额 =（固定资产原值 - 预计净残值）÷ 预计使用年限
     *
     * @param originalCost   固定资产原值
     * @param salvageValue   预计净残值
     * @param usefulLifeYears 预计使用年限（年）
     * @return 年折旧额
     * @throws IllegalArgumentException 如果参数无效（例如，usefulLifeYears <= 0）
     */
    public static BigDecimal calculateStraightLineDepreciation(BigDecimal originalCost, BigDecimal salvageValue, int usefulLifeYears) {
        validateInputs(originalCost, salvageValue, usefulLifeYears);

        if (usefulLifeYears <= 0) {
            throw new IllegalArgumentException("预计使用年限必须大于 0");
        }

        BigDecimal depreciableBase = originalCost.subtract(salvageValue);
        // 防止除数为零
        if (usefulLifeYears == 0) {
             return BigDecimal.ZERO; // 或者抛出异常，取决于业务逻辑
        }
        return depreciableBase.divide(BigDecimal.valueOf(usefulLifeYears), DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 计算工作量法的单位工作量折旧额
     * 公式：单位工作量折旧额 =（固定资产原值 - 预计净残值）÷ 预计总工作量
     *
     * @param originalCost       固定资产原值
     * @param salvageValue       预计净残值
     * @param totalProductionUnits 预计总工作量
     * @return 单位工作量折旧额
     * @throws IllegalArgumentException 如果参数无效（例如，totalProductionUnits <= 0）
     */
    public static BigDecimal calculateUnitsOfProductionDepreciationRate(BigDecimal originalCost, BigDecimal salvageValue, BigDecimal totalProductionUnits) {
         validateBasicInputs(originalCost, salvageValue);
         Objects.requireNonNull(totalProductionUnits, "预计总工作量不能为空");
        if (totalProductionUnits.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("预计总工作量必须大于 0");
        }

        BigDecimal depreciableBase = originalCost.subtract(salvageValue);
         // 防止除数为零
        if (totalProductionUnits.compareTo(BigDecimal.ZERO) == 0) {
             return BigDecimal.ZERO; // 或者抛出异常
        }
        // 计算比率时可以保留更多小数位以提高精度
        int rateScale = Math.max(DEFAULT_SCALE, 4) + 2;
        return depreciableBase.divide(totalProductionUnits, rateScale, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 计算工作量法的年折旧额
     * 公式：年折旧额 = 单位工作量折旧额 × 年实际工作量
     *
     * @param unitDepreciationRate 单位工作量折旧额 (通过 calculateUnitsOfProductionDepreciationRate 计算得到)
     * @param actualProductionUnits 年实际工作量
     * @return 年折旧额
     * @throws IllegalArgumentException 如果参数无效
     */
    public static BigDecimal calculateUnitsOfProductionAnnualDepreciation(BigDecimal unitDepreciationRate, BigDecimal actualProductionUnits) {
        Objects.requireNonNull(unitDepreciationRate, "单位工作量折旧额不能为空");
        Objects.requireNonNull(actualProductionUnits, "年实际工作量不能为空");
        if (actualProductionUnits.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("年实际工作量不能为负数");
        }
        return unitDepreciationRate.multiply(actualProductionUnits).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }


    /**
     * 计算双倍余额递减法的年折旧额
     * 注意：此方法计算的是 *某一年* 的折旧额，需要期初净值。
     * 在实际应用中，最后两年需要特殊处理，确保最终净值不低于残值。
     * 公式：
     * 年折旧率 = 2 ÷ 预计使用年限 × 100%
     * 年折旧额 = 固定资产期初净值 × 年折旧率
     *
     * @param beginningBookValue 固定资产期初净值 (原值 - 累计折旧)
     * @param salvageValue       预计净残值 (用于最后两年的调整判断，以及确保折旧后不低于残值)
     * @param usefulLifeYears    预计使用年限（年）
     * @param currentYear        当前是第几年 (从 1 开始)
     * @return 当年的折旧额
     * @throws IllegalArgumentException 如果参数无效
     */
    public static BigDecimal calculateDoubleDecliningBalanceDepreciation(BigDecimal beginningBookValue, BigDecimal salvageValue, int usefulLifeYears, int currentYear) {
        Objects.requireNonNull(beginningBookValue, "期初净值不能为空");
        Objects.requireNonNull(salvageValue, "预计净残值不能为空");
        if (usefulLifeYears <= 0) {
            throw new IllegalArgumentException("预计使用年限必须大于 0");
        }
         if (currentYear <= 0 || currentYear > usefulLifeYears) {
            throw new IllegalArgumentException("当前年份无效");
        }
        if (beginningBookValue.compareTo(salvageValue) < 0) {
             // 如果期初净值已低于残值，则不再折旧
             return BigDecimal.ZERO;
        }

        BigDecimal depreciationRate = BigDecimal.valueOf(2.0)
                .divide(BigDecimal.valueOf(usefulLifeYears), 4, DEFAULT_ROUNDING_MODE); // 提高折旧率精度

        BigDecimal annualDepreciation = beginningBookValue.multiply(depreciationRate);

        // 最后两年的特殊处理调整:
        // 如果是最后一年，折旧额 = 期初账面净值 - 预计净残值
        if (currentYear == usefulLifeYears) {
             annualDepreciation = beginningBookValue.subtract(salvageValue);
             // 确保折旧额不为负
             if (annualDepreciation.compareTo(BigDecimal.ZERO) < 0) {
                 annualDepreciation = BigDecimal.ZERO;
             }
             return annualDepreciation.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        }

        // 检查折旧后的账面价值是否会低于预计净残值
        BigDecimal bookValueAfterDepreciation = beginningBookValue.subtract(annualDepreciation);
        if (bookValueAfterDepreciation.compareTo(salvageValue) < 0) {
            // 如果折旧后低于残值，则本次折旧额调整为 期初净值 - 残值
            annualDepreciation = beginningBookValue.subtract(salvageValue);
             // 确保折旧额不为负
             if (annualDepreciation.compareTo(BigDecimal.ZERO) < 0) {
                 annualDepreciation = BigDecimal.ZERO;
             }
        }

         // 针对倒数第二年的特殊情况：如果按照正常计算，最后一年折旧后无法达到残值，
         // 很多会计实践中会将最后两年的折旧额调整为直线法处理剩余的可折旧金额
         // (beginningBookValue - salvageValue) / 2
         // 这里我们采用更常见的逐年计算并在最后一年强制调整到残值，
         // 或者当计算出的折旧额使得余额低于残值时进行调整。

        return annualDepreciation.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }


    /**
     * 计算年数总和法的年折旧额
     * 公式：
     * 年折旧率 = 剩余使用年限 ÷ 预计使用年限总和
     * 年折旧额 =（固定资产原值 - 预计净残值）× 年折旧率
     * 年数总和 = n * (n + 1) / 2，其中 n 是使用年限
     *
     * @param originalCost   固定资产原值
     * @param salvageValue   预计净残值
     * @param usefulLifeYears 预计使用年限（年）
     * @param currentYear    当前是第几年 (从 1 开始)
     * @return 当年的折旧额
     * @throws IllegalArgumentException 如果参数无效
     */
    public static BigDecimal calculateSumOfTheYearsDigitsDepreciation(BigDecimal originalCost, BigDecimal salvageValue, int usefulLifeYears, int currentYear) {
        validateInputs(originalCost, salvageValue, usefulLifeYears);
        if (currentYear <= 0 || currentYear > usefulLifeYears) {
            throw new IllegalArgumentException("当前年份无效，必须在 1 到 " + usefulLifeYears + " 之间");
        }

        BigDecimal depreciableBase = originalCost.subtract(salvageValue);

        // 计算年数总和: n * (n + 1) / 2
        long sumOfTheYears = (long) usefulLifeYears * (usefulLifeYears + 1) / 2;
         if (sumOfTheYears == 0) {
             return BigDecimal.ZERO; // 或抛出异常
         }

        // 计算剩余使用年限
        int remainingUsefulLife = usefulLifeYears - currentYear + 1;

        // 计算年折旧率 = 剩余使用年限 / 年数总和
        BigDecimal depreciationRate = BigDecimal.valueOf(remainingUsefulLife)
                .divide(BigDecimal.valueOf(sumOfTheYears), 4 + DEFAULT_SCALE, DEFAULT_ROUNDING_MODE); // 提高折旧率精度

        // 计算年折旧额 = (原值 - 残值) * 折旧率
        BigDecimal annualDepreciation = depreciableBase.multiply(depreciationRate);

        // 考虑累计折旧是否超过 (原值-残值)
        // 在实际应用中，需要跟踪累计折旧，确保总折旧不超过 depreciableBase
        // 这个方法只计算单一年份，调用者需要管理累计折旧

        return annualDepreciation.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }


    /**
     * 验证基本输入：原值和残值
     */
    private static void validateBasicInputs(BigDecimal originalCost, BigDecimal salvageValue) {
        Objects.requireNonNull(originalCost, "固定资产原值不能为空");
        Objects.requireNonNull(salvageValue, "预计净残值不能为空");

        if (originalCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("固定资产原值不能为负数");
        }
        if (salvageValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("预计净残值不能为负数");
        }
        if (salvageValue.compareTo(originalCost) > 0) {
            throw new IllegalArgumentException("预计净残值不能大于固定资产原值");
        }
    }

     /**
     * 验证包含年限的输入
     */
    private static void validateInputs(BigDecimal originalCost, BigDecimal salvageValue, int usefulLifeYears) {
        validateBasicInputs(originalCost, salvageValue);
        if (usefulLifeYears <= 0) {
            throw new IllegalArgumentException("预计使用年限必须大于 0");
        }
    }

} 