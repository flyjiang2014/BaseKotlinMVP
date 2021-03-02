package com.kotlin.mvp.model

/**
 * Created by  on 2021/1/14.
 * 文件说明：业务bean
 */

data class IndexShowBean(val accOrderPay:String ="", val price:String ="", val waitConsume:String ="", val waitShow:String ="",
                         val recvDays:String ="", val shopId:String ="", val shopName:String ="", val orderPay:String ="", val unreadNoticeNum:String ="",
                         val waitBack:String ="", val balanceAll:String ="", val portrait:String ="", val state:String ="",// 0已上线，1新建，9禁用
                         val  canUseIn:String ="",val hasPaypwd:String ="")
data class GuideInfoBean(
        val portrait:String = "",
        val portraitOther:String = "",
        val realName:String = "",
        val guideId:String = "",
        val mobile:String = "",
        val guideCard:String = "",
        val idCard:String = "",
        val sex:String = "",
        val cityId:String = "",
        val cityName:String = "",
        val provinceId:String = "",
        val provinceName:String = "",
        val beginYear:String = "",
        val feature:String = "",
        val description:String = "",
        val picPath:String = "",
        val picId:Int = 0,
        val state:String = "",
        val birthYear:String = "",
)