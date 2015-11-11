-- tabulka okresu
CREATE TABLE `okresy_actual` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- tabulka utvaru
CREATE TABLE `utvary_actual` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `ut` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;


INSERT INTO `2008ku`.`okresy_actual`
(`kr`,
`ok`,
`nazev`,
`doLink`,
`linkTo`)
VALUES
(<{kr: }>,
<{ok: }>,
<{nazev: }>,
<{doLink: }>,
<{linkTo: }>);
