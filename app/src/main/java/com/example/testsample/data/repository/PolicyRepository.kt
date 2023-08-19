package com.example.testsample.data.repository

import com.example.testsample.data.db.policy.PolicyDatabase
import com.example.testsample.data.model.PolicyModel

class PolicyRepository(private val policyDatabase: PolicyDatabase) {
    suspend fun addPolicy(policyModel: PolicyModel) =
        policyDatabase.getPolicyDao().insertPolicy(policyModel)

    suspend fun deletePolicy(policyModel: PolicyModel) =
        policyDatabase.getPolicyDao().deletePolicy(policyModel)

    suspend fun updatePolicy(policyModel: PolicyModel) =
        policyDatabase.getPolicyDao().updatePolicy(policyModel)


    suspend fun deleteAll() = policyDatabase.getPolicyDao().deleteAll()

    fun getAllPolicy() = policyDatabase.getPolicyDao().getAllPolicy()
}