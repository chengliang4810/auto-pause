ALTER TABLE torrent_progress RENAME TO _torrent_progress_old_20250307;
CREATE TABLE torrent_progress -- 种子进度 
(
key text NULL  , -- 唯一标识
platform text NULL  , -- 平台类型
name text NULL  , -- 种子的名称
size integer(16) NULL  , -- 种子的大小
hash text NULL   -- 种子的hash
);
INSERT INTO torrent_progress (key) SELECT key FROM _torrent_progress_old_20250307;
ALTER TABLE torrent_progress RENAME TO _torrent_progress_old_20250308;
CREATE TABLE torrent_progress -- 种子进度 
(
id text NULL  ,
key text NULL  , -- 唯一标识
platform text NULL  , -- 平台类型
name text NULL  , -- 种子的名称
size integer(16) NULL  , -- 种子的大小
hash text NULL   -- 种子的hash
);
INSERT INTO torrent_progress (size,name,key,platform,hash) SELECT size,name,key,platform,hash FROM _torrent_progress_old_20250308;
ALTER TABLE torrent_progress RENAME TO _torrent_progress_old_20250308_1;
CREATE TABLE torrent_progress -- 种子进度 
(
id text NULL  , -- 唯一标识
key text NULL  , -- 种子平台的唯一标识
platform text NULL  , -- 平台类型
name text NULL  , -- 种子的名称
size integer(16) NULL  , -- 种子的大小
hash text NULL   -- 种子的hash
);
INSERT INTO torrent_progress (size,name,id,key,platform,hash) SELECT size,name,id,key,platform,hash FROM _torrent_progress_old_20250308_1;
ALTER TABLE torrent_progress RENAME TO _torrent_progress_old_20250308_1_2;
CREATE TABLE torrent_progress -- 种子进度 
(
id text NULL  , -- 唯一标识
key text NULL  , -- 种子平台的唯一标识
platform text NULL  , -- 平台类型
name text NULL  , -- 种子的名称
size integer(16) NULL  , -- 种子的大小
hash text NULL  , -- 种子的hash
expire_time text NULL   -- 到期时间
);
INSERT INTO torrent_progress (size,name,id,key,platform,hash) SELECT size,name,id,key,platform,hash FROM _torrent_progress_old_20250308_1_2;
ALTER TABLE torrent_progress RENAME TO _torrent_progress_old_20250308_1_2_3;
CREATE TABLE torrent_progress -- 种子进度 
(
id text NULL  , -- 唯一标识
key text NULL  , -- 种子平台的唯一标识
platform text NULL  , -- 平台类型
name text NULL  , -- 种子的名称
size integer(16) NULL  , -- 种子的大小
hash text NULL  , -- 种子的hash
expire_time integer(16) NULL   -- 到期时间
);
INSERT INTO torrent_progress (size,name,expire_time,id,key,platform,hash) SELECT size,name,expire_time,id,key,platform,hash FROM _torrent_progress_old_20250308_1_2_3;
