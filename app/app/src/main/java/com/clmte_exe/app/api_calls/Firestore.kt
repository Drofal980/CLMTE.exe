package com.clmte_exe.app.api_calls

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreManager {

    companion object {
        const val COLLECTION_CARS = "cars"
        const val COLLECTION_CAR_TODOS = "car_todos"
        const val COLLECTION_GARAGE_CHAT = "garage_chat"
        const val COLLECTION_VEHICLES = "vehicles"

        private const val ROOT_COLLECTION = "app_data"
        private const val ROOT_DOCUMENT_ID = "single_user"
    }

    val db = FirebaseFirestore.getInstance()

    @PublishedApi
    internal fun scopedCollection(collection: String) =
        db.collection(ROOT_COLLECTION)
            .document(ROOT_DOCUMENT_ID)
            .collection(collection)

    suspend inline fun <reified T> getAllDocuments(
        collection: String
    ): List<T> {

        val snapshot = scopedCollection(collection)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(T::class.java)
        }
    }

    suspend inline fun <reified T> getDocument(
        collection: String,
        documentId: String
    ): T? {

        val snapshot = scopedCollection(collection)
            .document(documentId)
            .get()
            .await()

        return snapshot.toObject(T::class.java)
    }

    suspend inline fun <reified T> getDocumentsByField(
        collection: String,
        field: String,
        value: Any
    ): List<T> {

        val snapshot = scopedCollection(collection)
            .whereEqualTo(field, value)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(T::class.java)
        }
    }

    suspend fun <T : Any> saveDocument(
        collection: String,
        documentId: String,
        data: T
    ) {
        scopedCollection(collection)
            .document(documentId)
            .set(data)
            .await()
    }

    suspend fun deleteDocument(
        collection: String,
        documentId: String
    ) {
        scopedCollection(collection)
            .document(documentId)
            .delete()
            .await()
    }
}