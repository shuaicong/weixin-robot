package com.alter.wx.handler;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alter.utils.HttpClientUtil;
import com.alter.wx.buider.TextBuilder;

import me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
			WxSessionManager sessionManager) {

		if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
			// TODO 可以选择将消息保存到本地
		}
		//
		// //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
		// try {
		// if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
		// && weixinService.getKefuService().kfOnlineList()
		// .getKfOnlineList().size() > 0) {
		// return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
		// .fromUser(wxMessage.getToUser())
		// .toUser(wxMessage.getFromUser()).build();
		// }
		// } catch (WxErrorException e) {
		// e.printStackTrace();
		// }
		//
		// //TODO 组装回复消息
		// String content = "收到信息内容：" + JsonUtils.toJson(wxMessage);
		String content = "你说什么我听不见~";
		if (StringUtils.isNotEmpty(wxMessage.getContent())) {

			// 智能机器人API接口说明
			// 支持功能：天气、翻译、藏头诗、笑话、歌词、计算、域名信息/备案/收录查询、IP查询、手机号码归属、人工智能聊天
			// 接口地址：http://api.qingyunke.com/api.php?key=free&appid=0&msg=关键词
			// key 固定参数free
			// appid 设置为0，表示智能识别，可忽略此参数
			// msg 关键词，请参考下方参数示例，该参数可智能识别，该值请经过 urlencode 处理后再提交
			// 返回结果：{"result":0,"content":"内容"}
			// result 状态，0表示正常，其它数字表示错误
			// content 信息内容

			try {
				String resultStr = HttpClientUtil.httpGetRequest("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + wxMessage.getContent());
				logger.info(resultStr);
				JSONObject resultMap = JSONObject.parseObject(resultStr);
				int respCode = resultMap.getIntValue("result");
				if (respCode == 0) {
					content = resultMap.getString("content");
				}
			} catch (Exception e) {
				logger.error("消息发送失败:"+e);
			}

		}
		return new TextBuilder().build(content, wxMessage, weixinService);

	}

}
