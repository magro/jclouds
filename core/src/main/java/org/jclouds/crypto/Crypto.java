/**
 *
 * Copyright (C) 2011 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;

import javax.crypto.Mac;

import org.jclouds.encryption.internal.JCECrypto;

import com.google.inject.ImplementedBy;

/**
 * Allows you to access cryptographic objects and factories without adding a provider to the JCE
 * runtime.
 * 
 * @author Adrian Cole
 */
@ImplementedBy(JCECrypto.class)
public interface Crypto {

   KeyFactory rsaKeyFactory();

   CertificateFactory certFactory();

   Mac hmac(String algorithm, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException;

   Mac hmacSHA256(byte[] key) throws InvalidKeyException;

   Mac hmacSHA1(byte[] key) throws InvalidKeyException;

   MessageDigest digest(String algorithm) throws NoSuchAlgorithmException;

   MessageDigest md5();

   MessageDigest sha1();

   MessageDigest sha256();

}