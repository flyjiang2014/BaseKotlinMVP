package com.kotlin.mvp.adapter
/**
 * Created by  on 2020/12/18.
 * 文件说明：银行卡列表数据
 */
//class BankAdapter(var list:List<MyBankDataBean>): BaseQuickAdapter<MyBankDataBean, BaseViewHolder>(R.layout.item_bank_layout,list) {
//
//    var chooseId:String = ""
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }
//
//    constructor(list:List<MyBankDataBean>,chooseId:String) : this(list) {
//        this.chooseId = chooseId
//    }
//
//    override fun convert(helper: BaseViewHolder, item: MyBankDataBean?) {
//        item?:return
//        val tvBankName: TextView =helper.getView(R.id.tv_bank_name)
//        val tvBankCard: TextView = helper.getView(R.id.tv_bank_card)
//        val tvBankOwner: TextView = helper.getView(R.id.tv_bank_owner)
//        val imgPic: ImageView = helper.getView(R.id.img_pic)
//        val imgChoose: ImageView = helper.getView(R.id.img_choose)
//        tvBankName.text = item.bankName
//        tvBankCard.text = item.bankCardForShow
//        tvBankOwner.text = """持有人:${item.name}"""
//        GlideApp.with(mContext).load(item.picPath).into(imgPic)
//
//        if (!TextUtils.isEmpty(chooseId)) {
//            imgChoose.visibility = if(chooseId == item.id) View.VISIBLE else View.GONE
//        } else {
//            imgChoose.visibility = View.GONE
//        }
//    }
//}