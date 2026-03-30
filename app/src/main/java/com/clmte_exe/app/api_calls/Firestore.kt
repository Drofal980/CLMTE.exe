package com.clmte_exe.app.api_calls

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreManager {

    val db = FirebaseFirestore.getInstance()

    suspend inline fun <reified T> getAllDocuments(
        collection: String
    ): List<T> {

        val snapshot = db.collection(collection)
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

        val snapshot = db.collection(collection)
            .document(documentId)
            .get()
            .await()

        return snapshot.toObject(T::class.java)
    }

    suspend inline fun <reified T : Any> getDocumentsByField(
        collection: String,
        field: String,
        value: Any
    ): List<T> {
        val snapshot = db.collection(collection)
            .whereEqualTo(field, value)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(T::class.java)
        }
    }

    suspend fun <T> saveDocument(
        collection: String,
        documentId: String,
        data: T
    ) {
        db.collection(collection)
            .document(documentId)
            .set(data!!)
            .await()
    }

    suspend fun deleteDocument(
        collection: String,
        documentId: String
    ) {
        db.collection(collection)
            .document(documentId)
            .delete()
            .await()
    }
}