package com.bits.hr.util;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MimeTypeToFileType {

    private Hashtable<String, String> mimeExt = new Hashtable<String, String>();
    private Hashtable<String, String> extMime = new Hashtable<String, String>();

    public MimeTypeToFileType() {
        mimeExt.put("audio/aac", ".aac");
        mimeExt.put("application/x-abiword", ".abw");
        mimeExt.put("application/x-freearc", ".arc");
        mimeExt.put("video/x-msvideo", ".avi");
        mimeExt.put("application/vnd.amazon.ebook", ".azw");
        mimeExt.put("application/octet-stream", ".bin");
        mimeExt.put("image/bmp", ".bmp");
        mimeExt.put("application/x-bzip", ".bz");
        mimeExt.put("application/x-bzip2", ".bz2");
        mimeExt.put("application/x-cdf", ".cda");
        mimeExt.put("application/x-csh", ".csh");
        mimeExt.put("text/css", ".css");
        mimeExt.put("text/csv", ".csv");
        mimeExt.put("application/msword", ".doc");
        mimeExt.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");
        mimeExt.put("application/vnd.ms-fontobject", ".eot");
        mimeExt.put("application/epub+zip", ".epub");
        mimeExt.put("application/gzip", ".gz");
        mimeExt.put("image/gif", ".gif");
        mimeExt.put("text/html", ".html");
        mimeExt.put("image/vnd.microsoft.icon", ".ico");
        mimeExt.put("text/calendar", ".ics");
        mimeExt.put("application/java-archive", ".jar");
        mimeExt.put("image/jpeg", ".jpeg");
        mimeExt.put("text/javascript", ".js");
        mimeExt.put("application/json", ".json");
        mimeExt.put("application/ld+json", ".jsonld");
        mimeExt.put("audio/midi audio/x-midi", ".mid .midi");
        mimeExt.put("text/javascript", ".mjs");
        mimeExt.put("audio/mpeg", ".mp3");
        mimeExt.put("video/mp4", ".mp4");
        mimeExt.put("video/mpeg", ".mpeg");
        mimeExt.put("application/vnd.apple.installer+xml", ".mpkg");
        mimeExt.put("application/vnd.oasis.opendocument.presentation", ".odp");
        mimeExt.put("application/vnd.oasis.opendocument.spreadsheet", ".ods");
        mimeExt.put("application/vnd.oasis.opendocument.text", ".odt");
        mimeExt.put("audio/ogg", ".oga");
        mimeExt.put("video/ogg", ".ogv");
        mimeExt.put("application/ogg", ".ogx");
        mimeExt.put("audio/opus", ".opus");
        mimeExt.put("font/otf", ".otf");
        mimeExt.put("image/png", ".png");
        mimeExt.put("application/pdf", ".pdf");
        mimeExt.put("application/x-httpd-php", ".php");
        mimeExt.put("application/vnd.ms-powerpoint", ".ppt");
        mimeExt.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");
        mimeExt.put("application/vnd.rar", ".rar");
        mimeExt.put("application/rtf", ".rtf");
        mimeExt.put("application/x-sh", ".sh");
        mimeExt.put("image/svg+xml", ".svg");
        mimeExt.put("application/x-shockwave-flash", ".swf");
        mimeExt.put("application/x-tar", ".tar");
        mimeExt.put("image/tiff", ".tif .tiff");
        mimeExt.put("video/mp2t", ".ts");
        mimeExt.put("font/ttf", ".ttf");
        mimeExt.put("text/plain", ".txt");
        mimeExt.put("application/vnd.visio", ".vsd");
        mimeExt.put("audio/wav", ".wav");
        mimeExt.put("audio/webm", ".weba");
        mimeExt.put("video/webm", ".webm");
        mimeExt.put("image/webp", ".webp");
        mimeExt.put("font/woff", ".woff");
        mimeExt.put("font/woff2", ".woff2");
        mimeExt.put("application/xhtml+xml", ".xhtml");
        mimeExt.put("application/vnd.ms-excel", ".xls");
        mimeExt.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
        mimeExt.put("application/vnd.ms-excel.sheet.macroenabled.12", ".xlsm");
        mimeExt.put("application/xml", ".xml");
        mimeExt.put("application/vnd.mozilla.xul+xml", ".xul");
        mimeExt.put("application/zip", ".zip");
        mimeExt.put("video/3gpp", ".3gp");
        mimeExt.put("audio/3gpp", ".3gp");
        mimeExt.put("video/3gpp2", ".3g2");
        mimeExt.put("audio/3gpp2", ".3g3");
        mimeExt.put("application/x-7z-compressed", ".7z");

        extMime.put(".aac", "audio/aac");
        extMime.put(".abw", "application/x-abiword");
        extMime.put(".arc", "application/x-freearc");
        extMime.put(".avi", "video/x-msvideo");
        extMime.put(".azw", "application/vnd.amazon.ebook");
        extMime.put(".bin", "application/octet-stream");
        extMime.put(".bmp", "image/bmp");
        extMime.put(".bz", "application/x-bzip");
        extMime.put(".bz2", "application/x-bzip2");
        extMime.put(".cda", "application/x-cdf");
        extMime.put(".csh", "application/x-csh");
        extMime.put(".css", "text/css");
        extMime.put(".csv", "text/csv");
        extMime.put(".doc", "application/msword");
        extMime.put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        extMime.put(".eot", "application/vnd.ms-fontobject");
        extMime.put(".epub", "application/epub+zip");
        extMime.put(".gz", "application/gzip");
        extMime.put(".gif", "image/gif");
        extMime.put(".html", "text/html");
        extMime.put(".ico", "image/vnd.microsoft.icon");
        extMime.put(".ics", "text/calendar");
        extMime.put(".jar", "application/java-archive");
        extMime.put(".jpeg", "image/jpeg");
        extMime.put(".js", "text/javascript");
        extMime.put(".json", "application/json");
        extMime.put(".jsonld", "application/ld+json");
        extMime.put(".mid .midi", "audio/midi audio/x-midi");
        extMime.put(".mjs", "text/javascript");
        extMime.put(".mp3", "audio/mpeg");
        extMime.put(".mp4", "video/mp4");
        extMime.put(".mpeg", "video/mpeg");
        extMime.put(".mpkg", "application/vnd.apple.installer+xml");
        extMime.put(".odp", "application/vnd.oasis.opendocument.presentation");
        extMime.put(".ods", "application/vnd.oasis.opendocument.spreadsheet");
        extMime.put(".odt", "application/vnd.oasis.opendocument.text");
        extMime.put(".oga", "audio/ogg");
        extMime.put(".ogv", "video/ogg");
        extMime.put(".ogx", "application/ogg");
        extMime.put(".opus", "audio/opus");
        extMime.put(".otf", "font/otf");
        extMime.put(".png", "image/png");
        extMime.put(".pdf", "application/pdf");
        extMime.put(".php", "application/x-httpd-php");
        extMime.put(".ppt", "application/vnd.ms-powerpoint");
        extMime.put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        extMime.put(".rar", "application/vnd.rar");
        extMime.put(".rtf", "application/rtf");
        extMime.put(".sh", "application/x-sh");
        extMime.put(".svg", "image/svg+xml");
        extMime.put(".swf", "application/x-shockwave-flash");
        extMime.put(".tar", "application/x-tar");
        extMime.put(".tif .tiff", "image/tiff");
        extMime.put(".ts", "video/mp2t");
        extMime.put(".ttf", "font/ttf");
        extMime.put(".txt", "text/plain");
        extMime.put(".vsd", "application/vnd.visio");
        extMime.put(".wav", "audio/wav");
        extMime.put(".weba", "audio/webm");
        extMime.put(".webm", "video/webm");
        extMime.put(".webp", "image/webp");
        extMime.put(".woff", "font/woff");
        extMime.put(".woff2", "font/woff2");
        extMime.put(".xhtml", "application/xhtml+xml");
        extMime.put(".xls", "application/vnd.ms-excel");
        extMime.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        extMime.put(".xlsm", "application/vnd.ms-excel.sheet.macroenabled.12");
        extMime.put(".xml", "application/xml");
        extMime.put(".xul", "application/vnd.mozilla.xul+xml");
        extMime.put(".zip", "application/zip");
        extMime.put(".3gp", "video/3gpp");
        extMime.put(".3gp", "audio/3gpp");
        extMime.put(".3g2", "video/3gpp2");
        extMime.put(".3g3", "audio/3gpp2");
        extMime.put(".7z", "application/x-7z-compressed");
    }

    public String getFileExtension(String mimeType) {
        mimeType = mimeType.toLowerCase(Locale.ROOT);
        String fileExt = this.mimeExt.getOrDefault(mimeType, ".txt");
        if (fileExt == null) {
            return "";
        } else {
            return fileExt;
        }
    }

    public String getMimeType(String path) {
        String ext = getExtension(path).get().toLowerCase(Locale.ROOT);
        String fileExt = this.extMime.containsKey(ext) ? this.extMime.get(ext) : "application/octet-stream";
        if (fileExt == null) {
            return "";
        } else {
            return fileExt;
        }
    }

    private Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains(".")).map(f -> f.substring(filename.lastIndexOf(".") + 0));
    }
}
