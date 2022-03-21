package com.youngdatafan.dataintegration.core.util.encryption.sm;

import lombok.Getter;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECFieldElement.Fp;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * sm2加密实现类.
 *
 * @author gavin
 */
@Getter
public class Sm2 {

    /**
     * 国密推荐，正式参数.
     */
    public static final String[] ECC_PARAM = {
        "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF",
        "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC",
        "28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93",
        "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123",
        "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7",
        "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0",
    };

    private final BigInteger eccP;

    private final BigInteger eccA;

    private final BigInteger eccB;

    private final BigInteger eccN;

    private final BigInteger eccGx;

    private final BigInteger eccGy;

    private final ECCurve eccCurve;

    private final ECPoint eccPointG;

    private final ECDomainParameters eccBcSpec;

    private final ECKeyPairGenerator eccKeyPairGenerator;

    private final ECFieldElement eccGxFieldelement;

    private final ECFieldElement eccGyFieldelement;

    public Sm2() {
        this.eccP = new BigInteger(ECC_PARAM[0], 16);
        this.eccA = new BigInteger(ECC_PARAM[1], 16);
        this.eccB = new BigInteger(ECC_PARAM[2], 16);
        this.eccN = new BigInteger(ECC_PARAM[3], 16);
        this.eccGx = new BigInteger(ECC_PARAM[4], 16);
        this.eccGy = new BigInteger(ECC_PARAM[5], 16);

        this.eccGxFieldelement = new Fp(this.eccP, this.eccGx);
        this.eccGyFieldelement = new Fp(this.eccP, this.eccGy);

        this.eccCurve = new ECCurve.Fp(this.eccP, this.eccA, this.eccB);
        this.eccPointG = new ECPoint.Fp(this.eccCurve, this.eccGxFieldelement, this.eccGyFieldelement);

        this.eccBcSpec = new ECDomainParameters(this.eccCurve, this.eccPointG, this.eccN);

        ECKeyGenerationParameters eccEcgenparam = new ECKeyGenerationParameters(this.eccBcSpec, new SecureRandom());

        this.eccKeyPairGenerator = new ECKeyPairGenerator();
        this.eccKeyPairGenerator.init(eccEcgenparam);
    }

    /**
     * 获取sm2对象.
     *
     * @return Sm2
     */
    public static Sm2 getInstance() {
        return new Sm2();
    }

}
