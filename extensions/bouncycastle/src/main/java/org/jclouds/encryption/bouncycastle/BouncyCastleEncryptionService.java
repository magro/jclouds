/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
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
package org.jclouds.encryption.bouncycastle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.inject.Singleton;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.io.DigestOutputStream;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;
import org.jclouds.encryption.internal.BaseEncryptionService;
import org.jclouds.util.Utils;

import com.google.common.base.Throwables;
import com.google.common.io.Closeables;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class BouncyCastleEncryptionService extends BaseEncryptionService {

   @Override
   public byte[] hmacSha256(String toEncode, byte[] key) {
      return hmac(toEncode, key, new SHA256Digest());
   }

   @Override
   public byte[] hmacSha1(String toEncode, byte[] key) {
      return hmac(toEncode, key, new SHA1Digest());
   }

   public byte[] hmac(String toEncode, byte[] key, Digest digest) {
      HMac hmac = new HMac(digest);
      byte[] resBuf = new byte[hmac.getMacSize()];
      byte[] plainBytes = Utils.encodeString(toEncode);
      byte[] keyBytes = key;
      hmac.init(new KeyParameter(keyBytes));
      hmac.update(plainBytes, 0, plainBytes.length);
      hmac.doFinal(resBuf, 0);
      return resBuf;
   }

   @Override
   public byte[] md5(InputStream toEncode) {
      MD5Digest eTag = new MD5Digest();
      byte[] resBuf = new byte[eTag.getDigestSize()];
      byte[] buffer = new byte[1024];
      int numRead = -1;
      try {
         do {
            numRead = toEncode.read(buffer);
            if (numRead > 0) {
               eTag.update(buffer, 0, numRead);
            }
         } while (numRead != -1);
      } catch (IOException e) {
         throw new RuntimeException(e);
      } finally {
         Closeables.closeQuietly(toEncode);
      }
      eTag.doFinal(resBuf, 0);
      return resBuf;
   }

   @Override
   public String base64(byte[] resBuf) {
      return new String(Base64.encode(resBuf));
   }

   @Override
   public MD5InputStreamResult md5Result(InputStream toEncode) {
      MD5Digest eTag = new MD5Digest();
      byte[] resBuf = new byte[eTag.getDigestSize()];
      byte[] buffer = new byte[1024];
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      long length = 0;
      int numRead = -1;
      try {
         do {
            numRead = toEncode.read(buffer);
            if (numRead > 0) {
               length += numRead;
               eTag.update(buffer, 0, numRead);
               out.write(buffer, 0, numRead);
            }
         } while (numRead != -1);
      } catch (IOException e) {
         throw new RuntimeException(e);
      } finally {
         Closeables.closeQuietly(out);
         Closeables.closeQuietly(toEncode);
      }
      eTag.doFinal(resBuf, 0);
      return new MD5InputStreamResult(out.toByteArray(), resBuf, length);
   }

   @Override
   public byte[] fromBase64(String encoded) {
      return Base64.decode(encoded);
   }

   @Override
   public MD5OutputStream md5OutputStream(OutputStream out) {
      return new BouncyCastleMD5OutputStream(out);
   }

   private static class BouncyCastleMD5OutputStream extends MD5OutputStream {
      public BouncyCastleMD5OutputStream(OutputStream out) {
         super(new DigestOutputStream(out, new MD5Digest()));
      }

      @Override
      public byte[] getMD5() {
         MD5Digest digest = (MD5Digest) ((DigestOutputStream) out).getDigest();
         byte[] resBuf = new byte[digest.getDigestSize()];
         digest.doFinal(resBuf, 0);
         return resBuf;
      }
   }

   @Override
   public byte[] sha256(InputStream plainBytes) {
      return sha(plainBytes, new SHA256Digest());
   }

   @Override
   public byte[] sha1(InputStream plainBytes) {
      return sha(plainBytes, new SHA1Digest());
   }

   private byte[] sha(InputStream plainBytes, Digest digest) {
      byte[] resBuf = new byte[digest.getDigestSize()];
      byte[] buffer = new byte[1024];
      long length = 0;
      int numRead = -1;
      try {
         do {
            numRead = plainBytes.read(buffer);
            if (numRead > 0) {
               length += numRead;
               digest.update(buffer, 0, numRead);
            }
         } while (numRead != -1);
      } catch (IOException e) {
         throw new RuntimeException(e);
      } finally {
         Closeables.closeQuietly(plainBytes);
      }
      digest.doFinal(resBuf, 0);
      return resBuf;
   }

   @Override
   public byte[] rsaSign(String toSign, Key key) {
      // TODO convert this to BC code
      try {
         Cipher cipher = Cipher.getInstance("RSA");
         cipher.init(Cipher.ENCRYPT_MODE, key);
         return cipher.doFinal(toSign.getBytes());
      } catch (Exception e) {
         Throwables.propagate(e);
         return null;
      }
   }

}
