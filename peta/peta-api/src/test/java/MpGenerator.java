

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**  
 * @Description: 代码生成工具
 * @author: 若尘  
 * @date 2019年9月21日 下午4:03:07
 * @version V1.0  
 */
public class MpGenerator {
	static String dbName="jdbc:mysql://rm-wz9s5z7ax853tb40s5o.mysql.rds.aliyuncs.com:3306/peta_db?useUnicode=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai";
	static IdType idType = IdType.AUTO; //主键策略
	static String tableNameStr = "tb_mint_to_transfer";
	static String dbUserName = "peta_admin";
	static String dbPassword = "peta@666";
	static String[] tableNames = null;
	static String mapperPackageName="mapper"; 
	final static String dirPath = "/Users/yaozhen/Desktop/petacode";
	//final static String dirPath = "E://main//project-ing//peta";
	
//	static String dbName="jdbc:mysql://localhost:3306/dk-3";
//	static String mapperPackageName="mybatis.dao.mapping.db1"; 
//	static IdType idType= IdType.AUTO; //主键策略
//	static String[]  tableNames=new String[]{
//			"tb_user"
//			};
	
	/*
	sys_account
	,sys_menu
	,sys_role
	,sys_role_menu
	,sys_user_notification
	,sys_verify_sms_code
	,t_device_order_detail
	,t_order
	,t_pay_call_back
	,t_pay_record
	,tb_art_collection
	,tb_banner
	,tb_character_role
	,tb_collection_detail
	,tb_story
	,tb_story_chapter
	,tb_story_tales
	,tb_user
	,tb_virtual_character
	
	*/
	
	public static void main(String[] args) {
		AutoGenerator  generator=new AutoGenerator();
		
		//全局配置
		GlobalConfig  cfg=new GlobalConfig();
		cfg.setOutputDir(dirPath);
		cfg.setAuthor("若尘");
		cfg.setEnableCache(false);
		cfg.setBaseResultMap(true);
//		cfg.setBaseColumnList(true);
		
		//配置文件名
		cfg.setMapperName("%sMapper");
		cfg.setXmlName("%sMapper");
		cfg.setServiceName("%sService");
		cfg.setServiceImplName("%sServiceImpl");
		cfg.setControllerName("%sController");
		cfg.setEntityName("%sEntity");
		cfg.setIdType(idType);//主键策略
		cfg.setFileOverride(true);
		generator.setGlobalConfig(cfg);
		
		//配置数据源
		DataSourceConfig  dbCfg=new DataSourceConfig();
		dbCfg.setDbType(DbType.MYSQL);
		dbCfg.setTypeConvert(new MySqlTypeConvert(){
			 @Override
			    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
			        String t = fieldType.toLowerCase();
			        if (t.contains("char")) {
			            return DbColumnType.STRING;
			        } else if (t.contains("bigint")) {
			            return DbColumnType.INTEGER;
			        } else if (t.contains("tinyint(1)")) {
			            return DbColumnType.BYTE;
			        } else if (t.contains("int")) {
			            return DbColumnType.INTEGER;
			        } else if (t.contains("text")) {
			            return DbColumnType.STRING;
			        } else if (t.contains("bit")) {
			            return DbColumnType.BOOLEAN;
			        } else if (t.contains("decimal")) {
			            return DbColumnType.BIG_DECIMAL;
			        } else if (t.contains("clob")) {
			            return DbColumnType.CLOB;
			        } else if (t.contains("blob")) {
			            return DbColumnType.BLOB;
			        } else if (t.contains("binary")) {
			            return DbColumnType.BYTE_ARRAY;
			        } else if (t.contains("float")) {
			            return DbColumnType.FLOAT;
			        } else if (t.contains("double")) {
			            return DbColumnType.DOUBLE;
			        } else if (t.contains("json") || t.contains("enum")) {
			            return DbColumnType.STRING;
			        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
			            switch (globalConfig.getDateType()) {
			                case ONLY_DATE:
			                    return DbColumnType.DATE;
			                case SQL_PACK:
			                    switch (t) {
			                        case "date":
			                            return DbColumnType.DATE_SQL;
			                        case "time":
			                            return DbColumnType.TIME;
			                        case "year":
			                            return DbColumnType.DATE_SQL;
			                        default:
			                            return DbColumnType.TIMESTAMP;
			                    }
			                case TIME_PACK:
			                    switch (t) {
			                        case "date":
			                            return DbColumnType.LOCAL_DATE;
			                        case "time":
			                            return DbColumnType.LOCAL_TIME;
			                        case "year":
			                            return DbColumnType.YEAR;
			                        default:
			                            return DbColumnType.DATE;
			                    }
			            }
			        }
			        return DbColumnType.STRING;
			    }
		});
		dbCfg.setDriverName("com.mysql.jdbc.Driver");
		dbCfg.setUsername(dbUserName);
		dbCfg.setPassword(dbPassword);
		dbCfg.setUrl(dbName);
		generator.setDataSource(dbCfg);
		
		 // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(new String[] {"t_os_", "tb_", "sys_", "t_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        tableNames=tableNameStr.split(",");
         strategy.setInclude(tableNames); // 需要生成的表
         strategy.setEntityLombokModel(true);//使用Lombok生成getset
         strategy.setRestControllerStyle(true);
         strategy.setEntityTableFieldAnnotationEnable(true);
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
//         strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
         strategy.setSuperControllerClass("com.ltu.controller.BaseController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
         generator.setStrategy(strategy);
      // 包配置
         PackageConfig pc = new PackageConfig();
         pc.setParent("com.ltu");
//         pc.setModuleName("model.request");
         pc.setController("controller");
         pc.setEntity("domain.mp_entity");
         pc.setMapper(mapperPackageName);
         pc.setService("service");
         pc.setServiceImpl("service.impl");
         pc.setXml("sql");
         generator.setPackageInfo(pc);

         
         // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
         InjectionConfig  customCfg = new InjectionConfig() {
             @Override
             public void initMap() {
                 Map<String, Object> map = new HashMap<String, Object>();
                 map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                 this.setMap(map);
             }
         };
         
         // 自定义 xxList.jsp 生成
         List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
 /*        focList.add(new FileOutConfig("/template/list.jsp.vm") {
             @Override
             public String outputFile(TableInfo tableInfo) {
                 // 自定义输入文件名称
                 return "D://my_" + tableInfo.getEntityName() + ".jsp";
             }
         });
         cfg.setFileOutConfigList(focList);
         mpg.setCfg(cfg);*/
  
         // 调整 xml 生成目录演示
 /*        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
             @Override
             public String outputFile(TableInfo tableInfo) {
                 return dirPath + tableInfo.getEntityName() + "Mapper.xml";
             }
         });
         cfg.setFileOutConfigList(focList);
         */
         generator.setCfg(customCfg);
  
         // 关闭默认 xml 生成，调整生成 至 根目录
  
         // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
         // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
          TemplateConfig tc = new TemplateConfig();
          tc.setController("/templates/controller.java.vm");
          tc.setEntity("/templates/entity.java.vm");
          tc.setMapper("/templates/mapper.java.vm");
          tc.setXml("/templates/mapper.xml.vm");
          tc.setService("/templates/service.java.vm");
          tc.setServiceImpl("/templates/serviceImpl.java.vm");
         // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
          generator.setTemplate(tc);
  
         // 执行生成
         generator.execute();
  
         // 打印注入设置【可无】
//         System.err.println(mpg.getCfg().getMap().get("abc"));


	}

}
