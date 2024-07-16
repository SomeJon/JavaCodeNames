package exception.server;

import exception.CodeNameException;

public class TxtFileNotMatch extends CodeNameException {
    private final String FileName;
    private final String XmlName;

    public TxtFileNotMatch(String fileName, String xmlName) {
        this.FileName = fileName;
        this.XmlName = xmlName;
    }

    public String getFileName() {
        return FileName;
    }

    public String getXmlName() {
        return XmlName;
    }
}
