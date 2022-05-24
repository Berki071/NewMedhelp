//package com.medhelp.medhelp.data.db
//
//
//open class RealmMigration : RealmMigration {
//    fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
//        var oldVersion = oldVersion
//        val schema: RealmSchema = realm.getSchema()
//        if (oldVersion == 0L) {
//            oldVersion++
//        }
//        if (oldVersion == 1L) {
//            schema.create("NewsRealm")
//                .addField("id", Int::class.javaPrimitiveType, FieldAttribute.PRIMARY_KEY)
//                .addField("time", Long::class.javaPrimitiveType)
//            oldVersion++
//        }
//        if (oldVersion == 2L) {
//            schema.create("DataPaymentForRealm")
//                .addField("id", Int::class.javaPrimitiveType, FieldAttribute.PRIMARY_KEY)
//                .addField("idPayment", String::class.java)
//                .addField("yKey", String::class.java)
//                .addField("idUser", String::class.java)
//                .addField("idBranch", String::class.java)
//                .addField("idZapisi", String::class.java)
//                .addField("idYsl", String::class.java)
//                .addField("price", String::class.java)
//                .addField("isYandexInformation", Boolean::class.javaPrimitiveType)
//            oldVersion++
//        }
//        if (oldVersion == 3L) {
//            schema.get("DataPaymentForRealm")
//                .removeField("isYandexInformation")
//            oldVersion++
//        }
//        if (oldVersion == 4L) {
//            schema.get("DataPaymentForRealm")
//                .addField("isYandexInformation", Boolean::class.javaPrimitiveType)
//            oldVersion++
//        }
//    }
//
//    override fun hashCode(): Int {
//        return 37
//    }
//
//    override fun equals(o: Any?): Boolean {
//        return o is RealmMigration
//    }
//}