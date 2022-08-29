package com.dev.eventapp.models

data class Member(
    var uid: String?,
    var name: String?,
    var email: String?,
    var phoneNumber: String?,
    var photoUrl: String?,
    var coreTeam: Boolean? = false
)
