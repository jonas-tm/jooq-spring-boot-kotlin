package me.jonastm.jooqsample


import me.jonastm.jooqsample.adapter.out.persistence.entities.tables.references.USER_ACCOUNT
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*

@SpringBootApplication
@EnableTransactionManagement // jooq
class JooqSampleApplication

fun main(args: Array<String>) {
	runApplication<JooqSampleApplication>(*args)
}

fun getLogger(forClass: Class<*>): org.slf4j.Logger? =
	LoggerFactory.getLogger(forClass)

@Component
class AppStartupRunner(
	val dslContext: DSLContext
) : ApplicationRunner {

	companion object {
		private val LOG
				= getLogger(ApplicationRunner::class.java)!!
	}

	@Throws(Exception::class)
	override fun run(args: ApplicationArguments) {
		LOG.info("hello world")

		dslContext.deleteFrom(USER_ACCOUNT)
			.where(USER_ACCOUNT.EMAIL.eq("test@test.com"))
			.execute()

		dslContext.insertInto(USER_ACCOUNT)
			.set(USER_ACCOUNT.ID, UUID.randomUUID())
			.set(USER_ACCOUNT.EMAIL, "test@test.com")
			.set(USER_ACCOUNT.ENCODED_PASSWORD, "asd")
			.execute()

		val record = dslContext
			.selectFrom(USER_ACCOUNT)
			.where(USER_ACCOUNT.EMAIL.eq("test@test.com"))
			.orderBy(USER_ACCOUNT.ID)
			.firstOrNull()?.apply {

			}

		LOG.info("found {}", record.toString())
	}



}
