<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:ogr="http://ogr.maptools.org/"
  xmlns:gml="http://www.opengis.net/gml"
  >
  
  <xsl:output
    indent="no"
    encoding="utf-8"
    omit-xml-declaration="yes" />

  <xsl:strip-space elements="*" />
  
  <xsl:template match="*" mode="copy">
    <xsl:element name="{name()}" namespace="{namespace-uri()}">
      <xsl:apply-templates select="@*|node()" mode="copy" />
    </xsl:element>
  </xsl:template>

  <xsl:template match="@*|text()|comment()" mode="copy">
    <xsl:copy/>
  </xsl:template>
  
  <xsl:variable name="newline">
    <xsl:text>&#xa;</xsl:text>
  </xsl:variable>
  
  <xsl:template match="text()|@*" />
  
  <xsl:template match="/">
    <xsl:text>SET CLIENT_ENCODING TO 'UTF8';</xsl:text>
    <xsl:value-of select="$newline" />
    <xsl:apply-templates select="descendant::ogr:OOP_final" />  
  </xsl:template>
  
  <xsl:template match="ogr:OOP_final">
    <xsl:text>INSERT INTO gisdb.public.utvar_geom(valid_from, nazev_utvaru, kr, ok, ut, pocet_obyvatel, pocet_obyvatel_trvaly_pobyt, rozloha, nazev_okresniho_utvaru, nazev_krajskeho_utvaru, hranice) VALUES(</xsl:text>
    <xsl:text>TIMESTAMP '2012-03-01'</xsl:text>
    <xsl:text>, '</xsl:text>
    <xsl:value-of select="normalize-space(ogr:OOP_NAZEV)" />
    <xsl:text>', '</xsl:text>
    <xsl:value-of select="normalize-space(ogr:KR)" />
    <xsl:text>', '</xsl:text>
    <xsl:value-of select="normalize-space(ogr:OK)" />
    <xsl:text>', '</xsl:text>
    <xsl:value-of select="normalize-space(ogr:UT)" />
    <xsl:text>', </xsl:text>
    <xsl:value-of select="normalize-space(ogr:SUM_N_OBVY)" />
    <xsl:text>, </xsl:text>
    <xsl:value-of select="normalize-space(ogr:SUM_N_TRVA)" />
    <xsl:text>, </xsl:text>
    <xsl:value-of select="normalize-space(ogr:SUM_AREA_K)" />
    <xsl:text>, '</xsl:text>
    <xsl:value-of select="normalize-space(ogr:FIRST_DI_1)" />
    <xsl:text>', '</xsl:text>
    <xsl:value-of select="normalize-space(ogr:FIRST_CO_1)" />
    <xsl:text>', </xsl:text>
    <xsl:apply-templates select="ogr:geometryProperty" />
    <xsl:text>);</xsl:text>
    <xsl:value-of select="$newline" />
  </xsl:template>
  
  <xsl:template match="ogr:geometryProperty">
  
    <xsl:text>ST_GeomFromGML('</xsl:text>
      <xsl:apply-templates mode="copy" />
    <xsl:text>', 4326)</xsl:text>
    
  </xsl:template>

</xsl:stylesheet>