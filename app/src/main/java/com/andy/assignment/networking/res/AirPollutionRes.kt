package com.andy.assignment.networking.res


import com.google.gson.annotations.SerializedName

data class AirPollutionRes(
    @SerializedName("__extras")
    val extras: Extras,
    @SerializedName("fields")
    val fields: List<Field>,
    @SerializedName("include_total")
    val includeTotal: Boolean,
    @SerializedName("limit")
    val limit: String,
    @SerializedName("_links")
    val links: Links,
    @SerializedName("offset")
    val offset: String,
    @SerializedName("records")
    val records: List<Record>,
    @SerializedName("resource_format")
    val resourceFormat: String,
    @SerializedName("resource_id")
    val resourceId: String,
    @SerializedName("total")
    val total: String
)

data class Extras(
    @SerializedName("api_key")
    val apiKey: String
)

data class Field(
    @SerializedName("id")
    val id: String,
    @SerializedName("info")
    val info: Info,
    @SerializedName("type")
    val type: String
)


data class Info(
    @SerializedName("label")
    val label: String
)

data class Links(
    @SerializedName("next")
    val next: String,
    @SerializedName("start")
    val start: String
)

data class Record(
    @SerializedName("aqi")
    val aqi: String,
    @SerializedName("co")
    val co: String,
    @SerializedName("co_8hr")
    val co8hr: String,
    @SerializedName("county")
    val county: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("no")
    val no: String,
    @SerializedName("no2")
    val no2: String,
    @SerializedName("nox")
    val nox: String,
    @SerializedName("o3")
    val o3: String,
    @SerializedName("o3_8hr")
    val o38hr: String,
    @SerializedName("pm10")
    val pm10: String,
    @SerializedName("pm10_avg")
    val pm10Avg: String,
    @SerializedName("pm2.5")
    val pm25: String,
    @SerializedName("pm2.5_avg")
    val pm25Avg: String,
    @SerializedName("pollutant")
    val pollutant: String,
    @SerializedName("publishtime")
    val publishtime: String,
    @SerializedName("siteid")
    val siteid: String,
    @SerializedName("sitename")
    val sitename: String,
    @SerializedName("so2")
    val so2: String,
    @SerializedName("so2_avg")
    val so2Avg: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("wind_direc")
    val windDirec: String,
    @SerializedName("wind_speed")
    val windSpeed: String
)