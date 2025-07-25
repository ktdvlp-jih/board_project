/*
	DB 생성 MSSQL 
	버전 Microsoft SQL Server 2019
*/
USE [rsp_project]
GO

SET ANSI_PADDING ON
GO

CREATE NONCLUSTERED INDEX [IDX_RSP_BOARD_CONTENT] ON [dbo].[rsp_board]
(
	[board_content] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IDX_RSP_BOARD_STARTDATE_ENDDATE] ON [dbo].[rsp_board]
(
	[start_date] ASC,
	[end_date] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IDX_RSP_BOARD_TITLE] ON [dbo].[rsp_board]
(
	[board_title] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[rsp_attach_file](
	[attach_id] [bigint] NOT NULL,
	[original_filename] [varchar](255) NOT NULL,
	[save_filename] [varchar](255) NOT NULL,
	[file_path] [varchar](255) NULL,
	[file_size] [bigint] NOT NULL,
	[insert_id] [varchar](255) NULL,
	[insert_date] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[attach_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[rsp_attach_file] ADD  DEFAULT (NEXT VALUE FOR [rsp_attach_file_seq]) FOR [attach_id]
GO

ALTER TABLE [dbo].[rsp_attach_file] ADD  DEFAULT (getdate()) FOR [insert_date]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[rsp_board](
	[board_id] [bigint] NOT NULL,
	[board_title] [varchar](255) NULL,
	[board_content] [varchar](5000) NULL,
	[view_count] [bigint] NULL,
	[is_delete] [bit] NULL,
	[is_enable] [bit] NULL,
	[insert_id] [varchar](20) NULL,
	[insert_date] [datetime] NULL,
	[update_id] [varchar](20) NULL,
	[update_date] [datetime] NULL,
	[start_date] [datetime] NULL,
	[end_date] [datetime] NULL,
 CONSTRAINT [PK__rsp_boar__FB1C96E9AEFFE1B0] PRIMARY KEY CLUSTERED 
(
	[board_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[rsp_board] ADD  CONSTRAINT [DF__rsp_board__view___2D27B809]  DEFAULT ((0)) FOR [view_count]
GO

ALTER TABLE [dbo].[rsp_board] ADD  CONSTRAINT [DF__rsp_board__is_de__2E1BDC42]  DEFAULT ((0)) FOR [is_delete]
GO

ALTER TABLE [dbo].[rsp_board] ADD  CONSTRAINT [DF__rsp_board__is_en__2F10007B]  DEFAULT ((1)) FOR [is_enable]
GO

ALTER TABLE [dbo].[rsp_board] ADD  CONSTRAINT [DF__rsp_board__inser__300424B4]  DEFAULT (getdate()) FOR [insert_date]
GO

ALTER TABLE [dbo].[rsp_board] ADD  CONSTRAINT [DF__rsp_board__updat__30F848ED]  DEFAULT (getdate()) FOR [update_date]

CREATE TABLE [dbo].[rsp_code](
	[group_id] [varchar](3) NOT NULL,
	[code_id] [varchar](3) NOT NULL,
	[code_temp1] [varchar](100) NULL,
	[code_temp2] [varchar](100) NULL,
	[code_temp3] [varchar](100) NULL,
	[bigo] [varchar](100) NULL,
	[insert_id] [varchar](20) NULL,
	[insert_date] [datetime] NULL,
	[update_id] [varchar](20) NULL,
	[update_date] [datetime] NULL,
 CONSTRAINT [PK_rsp_code] PRIMARY KEY CLUSTERED 
(
	[group_id] ASC,
	[code_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[rsp_code] ADD  CONSTRAINT [DF_rsp_code_insert_date]  DEFAULT (getdate()) FOR [insert_date]
GO

ALTER TABLE [dbo].[rsp_code] ADD  CONSTRAINT [DF_rsp_code_update_date]  DEFAULT (getdate()) FOR [update_date]
GO


SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[rsp_link_file](
	[link_file_id] [bigint] NOT NULL,
	[ref_id] [bigint] NOT NULL,
	[attach_file_id] [bigint] NOT NULL,
	[is_delete] [bit] NULL,
	[insert_id] [varchar](20) NULL,
	[insert_date] [datetime] NULL,
	[update_id] [varchar](20) NULL,
	[update_date] [datetime] NULL,
 CONSTRAINT [PK__rsp_link__2C1570F6DE268411] PRIMARY KEY CLUSTERED 
(
	[link_file_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[rsp_link_file] ADD  CONSTRAINT [DF__rsp_link___link___17F790F9]  DEFAULT (NEXT VALUE FOR [rsp_link_file_seq]) FOR [link_file_id]
GO

ALTER TABLE [dbo].[rsp_link_file] ADD  CONSTRAINT [DF_rsp_link_file_is_delete]  DEFAULT ((0)) FOR [is_delete]
GO

ALTER TABLE [dbo].[rsp_link_file] ADD  CONSTRAINT [DF__rsp_link___inser__19DFD96B]  DEFAULT (getdate()) FOR [insert_date]
GO

ALTER TABLE [dbo].[rsp_link_file] ADD  CONSTRAINT [DF_rsp_link_file_update_date]  DEFAULT (getdate()) FOR [update_date]
GO


