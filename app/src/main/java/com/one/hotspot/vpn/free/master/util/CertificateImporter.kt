package com.one.hotspot.vpn.free.master.util

import android.content.Context
import org.spongycastle.util.encoders.Base64
import org.strongswan.android.logic.TrustedCertificateManager
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

object CertificateImporter {
    const val TAG = "CertificateImporter"

    fun import(certName: String): Boolean {
        try {
            val certificate: X509Certificate? = parseCertificate(certName)
            return storeCertificate(certificate)
        } catch (exception: Exception) {
             return false
        }
    }


    @Throws(CertificateException::class)
    private fun convertToX509Cert(certificateString: String?): X509Certificate? {
        val certificate: X509Certificate?
        val cf: CertificateFactory?
        try {
            val certificateData: ByteArray = Base64.decode(certificateString)
            cf = CertificateFactory.getInstance("X509")
            certificate = cf.generateCertificate(ByteArrayInputStream(certificateData)) as X509Certificate


        } catch (e: CertificateException) {
            throw CertificateException(e)
        }
        return certificate
    }


    private fun parseCertificate(certString: String): X509Certificate? {
        return convertToX509Cert(certString)
    }


    @Throws(IOException::class)
    private fun open(urlString: String?, context: Context): InputStream {
        val uri = URI.create(urlString)
        return if (uri.scheme == "file" && uri.path.startsWith("/android_asset/")) {
            val path = uri.path.replace("/android_asset/", "")
            context.assets.open(path)
        } else {
            uri.toURL().openStream()
        }
    }


    private fun storeCertificate(certificate: X509Certificate?): Boolean {
        return try {
            val store = KeyStore.getInstance("LocalCertificateStore")
            store.load(null, null)
            store.setCertificateEntry(null, certificate)
            TrustedCertificateManager.getInstance().reset()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}