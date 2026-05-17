-- Chuyển sang master trước khi xóa
USE master;
GO

-- Ngắt tất cả kết nối vào EnglishCenter
ALTER DATABASE EnglishCenter SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO

-- Xóa database cũ
DROP DATABASE EnglishCenter;
GO

-- Tạo mới
CREATE DATABASE EnglishCenter;
GO
USE EnglishCenter;
GO

-- BẢNG Users
CREATE TABLE Users (
    id            INT PRIMARY KEY IDENTITY(1,1),
    full_name     NVARCHAR(100) NOT NULL,
    email         VARCHAR(100)  NOT NULL UNIQUE,
    password_hash VARCHAR(255)  NOT NULL,
    role          VARCHAR(10)   NOT NULL CHECK (role IN ('admin','teacher','student','parent')),
    phone         VARCHAR(20),
    is_active     BIT           NOT NULL DEFAULT 1,
    created_at    DATETIME      NOT NULL DEFAULT GETDATE()
);
GO

-- BẢNG ClassLevels
CREATE TABLE ClassLevels (
    id           INT PRIMARY KEY IDENTITY(1,1),
    name         NVARCHAR(50)  NOT NULL,
    grade        INT           NOT NULL,
    sub_class    VARCHAR(10)   NOT NULL,
    year         INT           NOT NULL,
    status       VARCHAR(10)   NOT NULL DEFAULT 'ACTIVE'
                 CHECK (status IN ('ACTIVE','CLOSED')),
    monthly_fee  DECIMAL(12,2) NOT NULL,
    max_students INT           NOT NULL DEFAULT 20,
    CONSTRAINT UQ_Class UNIQUE (grade, sub_class, year)
);
GO

-- BẢNG ClassTeachers
CREATE TABLE ClassTeachers (
    id            INT PRIMARY KEY IDENTITY(1,1),
    class_id      INT NOT NULL FOREIGN KEY REFERENCES ClassLevels(id),
    teacher_id    INT NOT NULL FOREIGN KEY REFERENCES Users(id),
    assigned_date DATE NOT NULL DEFAULT GETDATE(),
    CONSTRAINT UQ_ClassTeacher UNIQUE (class_id, teacher_id)
);
GO

-- BẢNG Students
CREATE TABLE Students (
    id               INT PRIMARY KEY IDENTITY(1,1),
    user_id          INT          NOT NULL FOREIGN KEY REFERENCES Users(id),
    class_id         INT          NOT NULL FOREIGN KEY REFERENCES ClassLevels(id),
    enrollment_date  DATE         NOT NULL DEFAULT GETDATE(),
    fee_discount_pct DECIMAL(5,2) NOT NULL DEFAULT 0
                     CHECK (fee_discount_pct >= 0 AND fee_discount_pct <= 100),
    CONSTRAINT UQ_Student UNIQUE (user_id, class_id)
);
GO

-- BẢNG Parents
CREATE TABLE Parents (
    id              INT PRIMARY KEY IDENTITY(1,1),
    user_id         INT NOT NULL FOREIGN KEY REFERENCES Users(id),
    student_id      INT NOT NULL FOREIGN KEY REFERENCES Students(id),
    can_see_teacher BIT NOT NULL DEFAULT 1,
    CONSTRAINT UQ_Parent UNIQUE (user_id, student_id)
);
GO

-- BẢNG Sessions
CREATE TABLE Sessions (
    id           INT PRIMARY KEY IDENTITY(1,1),
    class_id     INT          NOT NULL FOREIGN KEY REFERENCES ClassLevels(id),
    session_date DATE         NOT NULL,
    notes        NVARCHAR(500),
    created_by   INT          NOT NULL FOREIGN KEY REFERENCES Users(id),
    created_at   DATETIME     NOT NULL DEFAULT GETDATE(),
    CONSTRAINT UQ_Session UNIQUE (class_id, session_date)
);
GO

-- BẢNG Attendance
CREATE TABLE Attendance (
    id            INT PRIMARY KEY IDENTITY(1,1),
    session_id    INT         NOT NULL FOREIGN KEY REFERENCES Sessions(id),
    student_id    INT         NOT NULL FOREIGN KEY REFERENCES Students(id),
    status        VARCHAR(10) NOT NULL CHECK (status IN ('PRESENT','ABSENT','EXCUSED')),
    absent_reason NVARCHAR(200),
    CONSTRAINT UQ_Attendance UNIQUE (session_id, student_id)
);
GO

-- BẢNG Payments
CREATE TABLE Payments (
    id           INT PRIMARY KEY IDENTITY(1,1),
    student_id   INT           NOT NULL FOREIGN KEY REFERENCES Students(id),
    month        INT           NOT NULL CHECK (month BETWEEN 1 AND 12),
    year         INT           NOT NULL CHECK (year >= 2020),
    amount_due   DECIMAL(12,2) NOT NULL,
    amount_paid  DECIMAL(12,2) NOT NULL DEFAULT 0,
    paid_date    DATE,
    pay_status   VARCHAR(10)   NOT NULL DEFAULT 'UNPAID'
                 CHECK (pay_status IN ('UNPAID','PARTIAL','PAID')),
    note         NVARCHAR(300),
    created_at   DATETIME      NOT NULL DEFAULT GETDATE(),
    CONSTRAINT UQ_Payment UNIQUE (student_id, month, year)
);
GO

-- BẢNG Announcements
CREATE TABLE Announcements (
    id           INT PRIMARY KEY IDENTITY(1,1),
    title        NVARCHAR(200) NOT NULL,
    content      NVARCHAR(MAX) NOT NULL,
    display_type VARCHAR(20)   NOT NULL DEFAULT 'popup'
                 CHECK (display_type IN ('popup','slider','banner')),
    image_url    VARCHAR(500),
    start_date   DATE          NOT NULL,
    end_date     DATE          NOT NULL,
    is_active    BIT           NOT NULL DEFAULT 1,
    created_by   INT           NOT NULL FOREIGN KEY REFERENCES Users(id),
    created_at   DATETIME      NOT NULL DEFAULT GETDATE()
);
GO

-- BẢNG Notifications
CREATE TABLE Notifications (
    id         INT PRIMARY KEY IDENTITY(1,1),
    parent_id  INT           NOT NULL FOREIGN KEY REFERENCES Parents(id),
    channel    VARCHAR(20)   NOT NULL CHECK (channel IN ('zalo','sms','facebook')),
    message    NVARCHAR(MAX) NOT NULL,
    status     VARCHAR(10)   NOT NULL DEFAULT 'PENDING'
               CHECK (status IN ('PENDING','SENT','FAILED')),
    sent_at    DATETIME,
    created_at DATETIME      NOT NULL DEFAULT GETDATE()
);
GO

-- ============================================
-- DỮ LIỆU MẪU
-- ============================================

INSERT INTO Users (full_name, email, password_hash, role, phone) VALUES
(N'Nguyễn Quản Trị', 'admin@trungtam.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin',   '0901000001'),
(N'Trần Thị Lan',    'lan@trungtam.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'teacher', '0901000002'),
(N'Lê Văn Minh',     'minh@trungtam.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'teacher', '0901000003'),
(N'Nguyễn Bảo An',  'an@trungtam.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'student', NULL),
(N'Phạm Minh Khoa', 'khoa@trungtam.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'student', NULL),
(N'Đặng Thúy Vy',   'vy@trungtam.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'student', NULL),
(N'Nguyễn Văn Bình','binh@gmail.com',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'parent',  '0908000001'),
(N'Phạm Thị Hoa',   'hoa@gmail.com',       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'parent',  '0908000002');
GO

INSERT INTO ClassLevels (name, grade, sub_class, year, monthly_fee, max_students) VALUES
(N'Lớp 3', 3, '3.1', 2024, 800000, 20),
(N'Lớp 3', 3, '3.2', 2024, 800000, 20),
(N'Lớp 5', 5, '5.1', 2024, 950000, 15);
GO

INSERT INTO ClassTeachers (class_id, teacher_id) VALUES
(1, 2), (2, 3), (3, 2);
GO

INSERT INTO Students (user_id, class_id, fee_discount_pct) VALUES
(4, 1,  0),
(5, 1, 20),
(6, 3,  0);
GO

INSERT INTO Parents (user_id, student_id, can_see_teacher) VALUES
(7, 1, 1),
(8, 2, 1);
GO

INSERT INTO Sessions (class_id, session_date, notes, created_by) VALUES
(1, '2024-03-04', N'Buổi 1 - Unit 1', 2),
(1, '2024-03-06', N'Buổi 2 - Unit 1', 2),
(1, '2024-03-11', N'Buổi 3 - Unit 2', 2);
GO

INSERT INTO Attendance (session_id, student_id, status, absent_reason) VALUES
(1, 1, 'PRESENT', NULL),
(1, 2, 'PRESENT', NULL),
(2, 1, 'PRESENT', NULL),
(2, 2, 'ABSENT',  N'Bị ốm'),
(3, 1, 'ABSENT',  N'Gia đình có việc'),
(3, 2, 'PRESENT', NULL);
GO

INSERT INTO Payments (student_id, month, year, amount_due, amount_paid, pay_status) VALUES
(1, 3, 2024, 300000, 300000, 'PAID'),
(2, 3, 2024, 240000,      0, 'UNPAID');
GO

-- ============================================
-- KIỂM TRA
-- ============================================
SELECT 'Users'        AS Bang, COUNT(*) AS SoBanGhi FROM Users       UNION ALL
SELECT 'ClassLevels',           COUNT(*)             FROM ClassLevels  UNION ALL
SELECT 'ClassTeachers',         COUNT(*)             FROM ClassTeachers UNION ALL
SELECT 'Students',              COUNT(*)             FROM Students      UNION ALL
SELECT 'Parents',               COUNT(*)             FROM Parents       UNION ALL
SELECT 'Sessions',              COUNT(*)             FROM Sessions      UNION ALL
SELECT 'Attendance',            COUNT(*)             FROM Attendance    UNION ALL
SELECT 'Payments',              COUNT(*)             FROM Payments;
GO

-- Kiểm tra sa có bị disabled không
SELECT name, is_disabled 
FROM sys.sql_logins 
WHERE name = 'sa';

-- Bật sa và đặt mật khẩu
ALTER LOGIN sa ENABLE;
ALTER LOGIN sa WITH PASSWORD = 'abc';
GO

-- Cập nhật hash mới cho tất cả user (mật khẩu vẫn là "123456")
USE EnglishCenter;
GO

UPDATE Users 
SET password_hash = '$2a$10$6yaDDNe9cMzjiEP0FxybcePOyNeRhUYhaweISjjStQgPTMPLlaQKG'
WHERE 1=1;
GO

SELECT email, password_hash FROM Users;

-- Kiểm tra lại
SELECT email, password_hash FROM Users;

USE EnglishCenter;
SELECT email, password_hash FROM Users WHERE email = 'admin@trungtam.com';