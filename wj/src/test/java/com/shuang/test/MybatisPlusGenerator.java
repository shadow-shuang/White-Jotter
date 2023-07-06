package com.shuang.test;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang.StringUtils;

import java.util.Scanner;

/**
 * @author ouyang
 */
public class MybatisPlusGenerator {
    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        //1、全局配置
        GlobalConfig gc = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
//        projectPathth = "F:\\meditrusthealthProject\\fast-insurance-admin-service\\fast-insurance-admin-service-web";

        String projectPath = "D:\\ideaProjects\\risk\\mth-insurance-intellect-pre-service\\mth-insurance-intellect-pre-service-web";

        //生成路径
        gc.setOutputDir(projectPath + "/src/main/java/");
        //设置作者
        gc.setAuthor("shuang.wu");
        gc.setOpen(false);
        //第二次生成会把第一次生成的覆盖
        gc.setFileOverride(true);
        //生成的service接口名字首字母是否为I，这样设置就没有
        gc.setServiceName("%sPlusService");
        gc.setServiceImplName("%sPlusServiceImpl");
        gc.setEntityName("%s");
        gc.setMapperName("%sPlusMapper");
        //生成resultMap
        gc.setBaseResultMap(true);
        mpg.setGlobalConfig(gc);

        //2、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://test-fast-mysql.meditrusthealth.com:3306/mth_insurance_intellect?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("fast");
        dsc.setPassword("mxjk@123");
        mpg.setDataSource(dsc);

        // 3、包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("");
        pc.setParent("");
        pc.setEntity("com.gm.wj.entity2");
        pc.setService("com.gm.wj.service2.plus");
        pc.setMapper("com.meditrusthealth.mth.insurance.intellect.mapper.plus");
        pc.setServiceImpl("com.meditrusthealth.mth.insurance.intellect.service.plus.impl");
        pc.setController("");
        pc.setXml("");
        pc.setXml("/resources/mapper/");
        mpg.setPackageInfo(pc);

        // 4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //使用lombok
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setEntityLombokModel(true);
        mpg.setStrategy(strategy);

        TemplateConfig templateConfig = new TemplateConfig();

        //控制 不生成 controller
        templateConfig.setController("");
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        //5、执行
        mpg.execute();
    }

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
