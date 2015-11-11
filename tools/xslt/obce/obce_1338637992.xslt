<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xsi:schemaLocation = "urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1 ../ruian/xsd/vymenny_format/VymennyFormatTypy.xsd" xmlns:gml = "http://www.opengis.net/gml/3.2" xmlns:xlink = "http://www.w3.org/1999/xlink" xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance" xmlns:ami = "urn:cz:isvs:ruian:schemas:AdrMistoIntTypy:v1" xmlns:base = "urn:cz:isvs:ruian:schemas:BaseTypy:v1" xmlns:coi = "urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1" xmlns:com = "urn:cz:isvs:ruian:schemas:CommonTypy:v1" xmlns:kui = "urn:cz:isvs:ruian:schemas:KatUzIntTypy:v1" xmlns:kri = "urn:cz:isvs:ruian:schemas:KrajIntTypy:v1" xmlns:mci = "urn:cz:isvs:ruian:schemas:MomcIntTypy:v1" xmlns:mpi = "urn:cz:isvs:ruian:schemas:MopIntTypy:v1" xmlns:obi = "urn:cz:isvs:ruian:schemas:ObecIntTypy:v1" xmlns:oki = "urn:cz:isvs:ruian:schemas:OkresIntTypy:v1" xmlns:opi = "urn:cz:isvs:ruian:schemas:OrpIntTypy:v1" xmlns:pai = "urn:cz:isvs:ruian:schemas:ParcelaIntTypy:v1" xmlns:pui = "urn:cz:isvs:ruian:schemas:PouIntTypy:v1" xmlns:rsi = "urn:cz:isvs:ruian:schemas:RegSouIntiTypy:v1" xmlns:spi = "urn:cz:isvs:ruian:schemas:SpravObvIntTypy:v1" xmlns:sti = "urn:cz:isvs:ruian:schemas:StatIntTypy:v1" xmlns:soi = "urn:cz:isvs:ruian:schemas:StavObjIntTypy:v1" xmlns:uli = "urn:cz:isvs:ruian:schemas:UliceIntTypy:v1" xmlns:vci = "urn:cz:isvs:ruian:schemas:VuscIntTypy:v1" xmlns:vf = "urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1" xmlns:zji = "urn:cz:isvs:ruian:schemas:ZsjIntTypy:v1" xmlns:voi = "urn:cz:isvs:ruian:schemas:VOIntTypy:v1"
  >
  
  <xsl:output
    indent="no"
    encoding="utf-8"
    omit-xml-declaration="yes"
    media-type="text/plain;charset=utf-8" />
  
  <xsl:strip-space elements="*" />
  
  <xsl:variable name="newline">
    <xsl:text>&#xa;</xsl:text>
  </xsl:variable>
  
  <xsl:variable name="wgs84_srid">
    <xsl:text>4326</xsl:text>
  </xsl:variable>
  <xsl:variable name="jtsk_srid">
    <xsl:text>5514</xsl:text>
  </xsl:variable>
  
  <xsl:template match="*" mode="copy">
    <xsl:element name="{name()}" namespace="{namespace-uri()}">
      <xsl:apply-templates select="@*|node()" mode="copy" />
    </xsl:element>
  </xsl:template>

  <xsl:template match="@*|text()|comment()" mode="copy">
    <xsl:copy/>
  </xsl:template>
  
  <xsl:template match="text()|@*" />
  
  <xsl:template match="/">
    <xsl:text>SET CLIENT_ENCODING TO 'UTF8';</xsl:text>
    <xsl:value-of select="$newline" />
    <xsl:apply-templates select="descendant::vf:Obec" />  
  </xsl:template>
  
  <xsl:template match="vf:Obec">
    <xsl:text>INSERT INTO gisdb.public.obec_ruian(valid_from, kod, nazev, okres_kod, pou_kod, plati_od, definicni_bod</xsl:text>
    <xsl:if test=".//obi:OriginalniHranice">
      <xsl:text>, hranice</xsl:text>
    </xsl:if>
    <xsl:text>) VALUES(</xsl:text>
    <xsl:text>TIMESTAMP '2012-06-02 11:53:12'</xsl:text>
    <xsl:text>,</xsl:text>
    <xsl:value-of select="normalize-space(obi:Kod)" />
    <xsl:text>, '</xsl:text>
    <xsl:value-of select="normalize-space(obi:Nazev)" />
    <xsl:text>', </xsl:text>
    <xsl:value-of select="normalize-space(descendant::oki:Kod)" />
    <xsl:text>, </xsl:text>
    <xsl:value-of select="normalize-space(descendant::pui:Kod)" />
    <xsl:text>, '</xsl:text>
    <xsl:value-of select="normalize-space(obi:PlatiOd)" />
    <xsl:text>', </xsl:text>
    
    <xsl:apply-templates select=".//obi:DefinicniBod" />
    <xsl:if test=".//obi:OriginalniHranice">
      <xsl:text>, </xsl:text>
      <xsl:apply-templates select=".//obi:OriginalniHranice" />
    </xsl:if>
    
    <xsl:text>);</xsl:text>
    
    <xsl:value-of select="$newline" />
  </xsl:template>
  
  <xsl:template match="obi:DefinicniBod">
    <xsl:text>ST_Transform(ST_GeomFromGML('</xsl:text>
    <xsl:apply-templates mode="copy" />                  
    <xsl:text>',</xsl:text>
    <xsl:value-of select="$jtsk_srid" />
    <xsl:text>), </xsl:text>
    <xsl:value-of select="$wgs84_srid" />
    <xsl:text>)</xsl:text>
  </xsl:template>
  
  <xsl:template match="obi:OriginalniHranice">
  
    <xsl:choose>
       <xsl:when test="boolean(.//gml:Ring)">
        <xsl:text>ST_Transform(ST_GeomFromEWKT('SRID=</xsl:text><xsl:value-of select="$jtsk_srid" /><xsl:text>;</xsl:text>
        <xsl:apply-templates mode="curves" />                  
        <xsl:text>'),</xsl:text>
        <xsl:value-of select="$wgs84_srid" />
        <xsl:text>)</xsl:text>
       </xsl:when>
       <xsl:otherwise>
        <xsl:text>ST_Transform(ST_GeomFromGML('</xsl:text>
        <xsl:apply-templates mode="copy" />
        <xsl:text>',</xsl:text>
        <xsl:value-of select="$jtsk_srid" />
        <xsl:text>), </xsl:text>
        <xsl:value-of select="$wgs84_srid" />
        <xsl:text>)</xsl:text>
       </xsl:otherwise>
    </xsl:choose>
  
  </xsl:template>
  
  <xsl:template match="gml:MultiSurface" mode="curves">
    <xsl:text>MULTISURFACE(</xsl:text>
      <xsl:for-each select="gml:surfaceMember">
        <xsl:apply-templates mode="curves" />
        <xsl:if test="position() != last()">
          <xsl:text>,</xsl:text>
        </xsl:if>
      </xsl:for-each>
    <xsl:text>)</xsl:text>
  </xsl:template>
  
  <xsl:template match="gml:Polygon" mode="curves">
    <xsl:text>CURVEPOLYGON(</xsl:text><xsl:apply-templates select="gml:exterior" mode="curves" />
      <xsl:for-each select="gml:interior">
        <xsl:text>, </xsl:text>
        <xsl:apply-templates mode="curves" />
      </xsl:for-each>
    <xsl:text>)</xsl:text>
  </xsl:template>
  
  <xsl:template match="gml:exterior | gml:interior" mode="curves">
    <xsl:apply-templates mode="curves" />
  </xsl:template>
  
  <xsl:template match="gml:Ring" mode="curves">
    <xsl:text>COMPOUNDCURVE(</xsl:text>
      <xsl:for-each select="gml:curveMember">
        <xsl:apply-templates mode="curves" />
        <xsl:if test="position() != last()">
          <xsl:text>,</xsl:text>
        </xsl:if>
      </xsl:for-each>
    <xsl:text>)</xsl:text>
  </xsl:template>
  
  <xsl:template match="gml:LineString" mode="curves">
    <xsl:text>LINESTRING(</xsl:text>
      <xsl:apply-templates select="gml:posList" mode="curves" />
    <xsl:text>)</xsl:text>
  </xsl:template>
  
  <xsl:template match="gml:Curve" mode="curves">
    <xsl:for-each select="gml:segments/child::node()">
        <xsl:apply-templates select="." mode="curves" />
        <xsl:if test="position() != last()">
          <xsl:text>,</xsl:text>
        </xsl:if>
      </xsl:for-each>
  </xsl:template>
  
  <xsl:template match="gml:ArcString" mode="curves">
    <xsl:text>CIRCULARSTRING(</xsl:text>
      <xsl:apply-templates select="gml:posList" mode="curves" />
    <xsl:text>)</xsl:text>
  </xsl:template>
  
  <xsl:template match="gml:posList" mode="curves">
    
    <xsl:for-each select="tokenize(normalize-space(.), '\s')">
      <xsl:value-of select="." /><xsl:text> </xsl:text>
      <xsl:if test="(position() mod 2) = 0">
        <xsl:if test="position() != last()">
          <xsl:text>,</xsl:text>
        </xsl:if>
      </xsl:if>
    </xsl:for-each>
    
  </xsl:template>

</xsl:stylesheet>