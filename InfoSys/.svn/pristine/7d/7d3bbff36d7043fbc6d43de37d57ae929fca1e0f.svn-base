package com.code.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Hex;

import com.code.exceptions.BusinessException;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import java.util.Base64;

/**
 * @author Ali Ismail Version 2.0
 */
public class EncryptionUtil {
	private static EncryptionUtil encUtil = null;
	/**
	 * String to hold name of the encryption algorithm.
	 */
	private static final String ALGORITHM = "RSA";
	private static final int KEY_LENGTH = 1024;
	/**
	 * String to hold the name of the private key file.
	 */
	private static final String PRIVATE_KEY_FILE = InfoSysConfigurationService.getReportsRoot() + "/keys/private.key";
	/**
	 * String to hold name of the public key file.
	 */
	private static final String PUBLIC_KEY_FILE = InfoSysConfigurationService.getReportsRoot() + "/keys/public.key";
	/**
	 * Key public and private keys
	 */
	private static PrivateKey privateKey = null;
	private static PublicKey publicKey = null;
	// -----------------------------------------------------------------------------------------
	private final static String algAESSerectKey = "Zp7Yx26TZigp6kBppwp+Aw==";

	@SuppressWarnings("resource")
	private EncryptionUtil() throws BusinessException {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
			publicKey = (PublicKey) inputStream.readObject();

			inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
			privateKey = (PrivateKey) inputStream.readObject();
			inputStream.close();
		} catch (Exception e) {
			Log4j.traceErrorException(EncryptionUtil.class, e, "EncryptionUtil");
			throw new BusinessException("error_general");
		}
	}

	public static EncryptionUtil getInstance() throws BusinessException {
		if (encUtil == null)
			encUtil = new EncryptionUtil();

		return encUtil;
	}

	/**
	 * Generate key which contains a pair of private and public key using 1024 bytes.
	 */
	private void genereteAsymmetricKey() {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
			keyGen.initialize(KEY_LENGTH);
			KeyPair key = keyGen.generateKeyPair();

			URL url = getClass().getClassLoader().getResource(PRIVATE_KEY_FILE);
			File privateKeyFile = new File(url.getPath());
			url = getClass().getClassLoader().getResource(PUBLIC_KEY_FILE);
			File publicKeyFile = new File(url.getPath());

			// Create files to store public and private key
			if (privateKeyFile.getParentFile() != null) {
				privateKeyFile.getParentFile().mkdirs();
			}
			privateKeyFile.createNewFile();

			if (publicKeyFile.getParentFile() != null) {
				publicKeyFile.getParentFile().mkdirs();
			}
			publicKeyFile.createNewFile();

			// Saving the Public key in a file
			ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
			publicKeyOS.writeObject(key.getPublic());
			publicKeyOS.close();

			// Saving the Private key in a file
			ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(key.getPrivate());
			privateKeyOS.close();
		} catch (Exception e) {
			Log4j.traceErrorException(EncryptionUtil.class, e, "EncryptionUtil");
		}
	}

	/**
	 * Encrypt the plain text using public key.
	 * 
	 * @param text
	 *            : original plain text
	 * @param publicKeyResourcePath
	 *            :The public key for third parties
	 * @return
	 */
	public String encryptAsymmetrically(String text, String... publicKeyResourcePath) {
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// encrypt the plain text using the public key
			if (publicKeyResourcePath.length == 0 || publicKeyResourcePath == null)
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			else {
				String encryptionFilePath = InfoSysConfigurationService.getReportsRoot() + publicKeyResourcePath[0].toString();
				ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(encryptionFilePath));
				PublicKey pk = (PublicKey) inputStream.readObject();
				cipher.init(Cipher.ENCRYPT_MODE, pk);
			}
			byte[] cipherText = cipher.doFinal(text.getBytes("UTF-8"));
			// encode the encrypted byte array via hex encoding then return it
			// as a string
			return new String(Hex.encodeHex(cipherText));
		} catch (Exception e) {
			Log4j.traceErrorException(EncryptionUtil.class, e, "EncryptionUtil");
		}
		return null;
	}

	/**
	 * Decrypt text using private key.
	 * 
	 * @param text
	 *            :encrypted text
	 * @param key
	 *            :The private key
	 * @return plain text
	 * @throws java.lang.Exception
	 */
	public String decryptAsymmetrically(String encoded) {
		byte[] dectyptedText = null;
		try {
			// Decode the incoming encoded encrypted string to extract encrypted
			// byte array
			byte[] encryptedBytes = Hex.decodeHex(encoded.toCharArray());
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// decrypt the text using the private key
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			dectyptedText = cipher.doFinal(encryptedBytes);
			// return the decrypted string
			return new String(dectyptedText, "UTF-8");
		} catch (Exception ex) {
			Log4j.traceErrorException(EncryptionUtil.class, ex, "EncryptionUtil");
		}
		return null;
	}

	// ------------------------------Symmetric Encryption------------------------
	/**
	 * Encrypts plainText in AES using the secret key
	 * 
	 * @param plainText
	 * @param secKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptSymmetrically(String plainText) throws Exception {
		Cipher aesCipher = Cipher.getInstance("AES");
		byte[] encodedKey = Base64.getDecoder().decode(algAESSerectKey);
		SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
		aesCipher.init(Cipher.ENCRYPT_MODE, originalKey);
		byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes("UTF-8"));
		return DatatypeConverter.printHexBinary(byteCipherText);
	}

	/**
	 * Decrypts encrypted byte array using the key used for encryption.
	 * 
	 * @param byteCipherText
	 * @param secKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptSymmetrically(String cipherText) {
		try {
			Cipher aesCipher = Cipher.getInstance("AES");
			byte[] encodedKey = Base64.getDecoder().decode(algAESSerectKey);
			SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
			aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
			byte[] bytePlainText = aesCipher.doFinal(DatatypeConverter.parseHexBinary(cipherText));
			return new String(bytePlainText, "UTF-8");
		} catch (Exception e) {
			Log4j.traceErrorException(EncryptionUtil.class, e, "EncryptionUtil");
			return null;
		}
	}

	/**
	 * Generate Symmetric key
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Key generateSymmetricKey() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		SecretKey key = generator.generateKey();
		return key;
	}
}