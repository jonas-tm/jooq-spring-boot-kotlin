//package me.jonastm.jooqsample.config
//
//import org.jooq.ExecuteContext
//import org.jooq.ExecuteListener
//import org.jooq.impl.*
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
//import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
//import org.springframework.jdbc.support.SQLExceptionTranslator
//import javax.sql.DataSource
//
//
//@Configuration
//class JooqConfiguration {
//
//    @Autowired
//    private final lateinit var dataSource: DataSource
//
//    @Bean
//    fun connectionProvider(): DataSourceConnectionProvider {
//        return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))
//    }
//
//    @Bean
//    fun dsl(): DefaultDSLContext {
//        val jooqConfiguration = DefaultConfiguration()
//        jooqConfiguration.set(connectionProvider())
//        jooqConfiguration
//            .set(DefaultExecuteListenerProvider(ExceptionTranslator()))
//
//        return DefaultDSLContext(jooqConfiguration)
//    }
//
//}
//
//
//class ExceptionTranslator : ExecuteListener {
//    override fun exception(context: ExecuteContext) {
//        val dialect = context.configuration().dialect()
//        val translator: SQLExceptionTranslator =
//            SQLErrorCodeSQLExceptionTranslator(dialect.thirdParty().springDbName()!!)
//        context.exception(translator.translate("Access database using jOOQ", context.sql(), context.sqlException()!!))
//    }
//}