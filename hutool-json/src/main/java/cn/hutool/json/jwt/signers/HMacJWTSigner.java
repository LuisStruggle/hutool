package cn.hutool.json.jwt.signers;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.crypto.digest.HMac;

import java.nio.charset.Charset;
import java.security.Key;

/**
 * HMac算法签名实现
 *
 * @author looly
 * @since 5.7.0
 */
public class HMacJWTSigner implements JWTSigner {

	private Charset charset = CharsetUtil.UTF_8;
	private final HMac hMac;

	/**
	 * 构造
	 *
	 * @param algorithm HMAC签名算法
	 * @param key       密钥
	 */
	public HMacJWTSigner(final String algorithm, final byte[] key) {
		this.hMac = new HMac(algorithm, key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm HMAC签名算法
	 * @param key       密钥
	 */
	public HMacJWTSigner(final String algorithm, final Key key) {
		this.hMac = new HMac(algorithm, key);
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 * @return 编码
	 */
	public HMacJWTSigner setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public String sign(final String headerBase64, final String payloadBase64) {
		return hMac.digestBase64(StrUtil.format("{}.{}", headerBase64, payloadBase64), charset, true);
	}

	@Override
	public boolean verify(final String headerBase64, final String payloadBase64, final String signBase64) {
		final String sign = sign(headerBase64, payloadBase64);
		return hMac.verify(
				StrUtil.bytes(sign, charset),
				StrUtil.bytes(signBase64, charset));
	}

	@Override
	public String getAlgorithm() {
		return this.hMac.getAlgorithm();
	}
}