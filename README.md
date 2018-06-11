
### ��������

| ����    | �汾������                |
| ----- | -------------------- |
| OS    | Windows 10            |
| JDK   | 1.8+                 |
| IDE   | IntelliJ IDEA 2017.3 |
| Maven | 3.3.1                |
| MySQL | 5.6.4                |

### ģ�黮��

| ģ��         | ����                      |
| ---------- | ----------------------- |
| blog-core  | ����ҵ����ģ�飬�ṩ���������ݲ��������ߴ���� |
| blog-admin | ��̨����ģ��                  |
| blog-web   | ǰ̨ģ��                    |


### ����ջ

- Springboot 1.5.9
- Apache Shiro 1.2.2
- Logback
- Redis
- Lombok
- Websocket
- MySQL��Mybatis��Mapper��Pagehelper
- Freemarker
- Bootstrap 3.3.0
- wangEditor
- jQuery 1.11.1��jQuery Lazyload 1.9.7��fancybox��iCheck
- ������OSS
- kaptcha
- Qiniu
- ...


### ʹ�÷���

1. ʹ��IDE���뱾��Ŀ
2. �½����ݿ�`CREATE DATABASE dblog;`
3. �������ݿ�`docs/db/dblog.sql`
4. �޸�(`resources/application.yml`)�����ļ�
   1. ���ݿ���������(������`datasource`��λ��L.19) 
   2. redis����(������`redis`��λ��L.69)
   3. mail����(������`mail`��λ��L.89)
5. ������Ŀ(���ַ�ʽ)
   1. ��Ŀ��Ŀ¼��ִ��`mvn -X clean package -Dmaven.test.skip=true`��������Ȼ��ִ��`java -jar target/blog-web.jar`
   2. ��Ŀ��Ŀ¼��ִ��`mvn springboot:run`
   3. ֱ������`BlogWebApplication.java`
6. ���������`http://127.0.0.1:8443`


**��̨�û�**

_��������Ա_�� �˺ţ�root  ���룺123456  �����ز���ʹ������˺ţ�adminû����Ȩ�ޣ�

_��ͨ����Ա_�� �˺ţ�admin  ���룺123456

_������˹���Ա_�� �˺ţ�comment-admin  ���룺123456

ע����̨�û��Ĵ���������������**Ȩ����С��**


### ������־

2018-05-25

**�޸Ĺ��ܣ�**

1. �޸���̨��ǩ�ȷ�ҳʧ�ܵ�����
2. �޸�ǰ̨�Զ���������ʧ�ܵ�����
3. ����һЩ����


2018-05-22

**�޸Ĺ��ܣ�**

1. ����shiroȨ�ޣ����ݿ⡢ҳ�棩��ע����Ҫ����ִ����`sys_resources`��`sys_role_resources`���ű��`insert`���
2. redis����Ĭ�ϲ������루���ڴ�������ѵ�redis��û�����������޸ģ��������� **ǿ�ҽ���**���������룩

2018-05-18

**�޸�bug��**

1. web���Զ�������������ʾ������
2. config���޸ĺ���ʵʱˢ�µ�����
	
**���ӹ��ܣ�**
1. ��վ������
2. �ٶ����͹���(�����ύ���ٶ�վ��ƽ̨)
	
**�޸Ĺ��ܣ�**
1. �ٶ�api��ak�Ͱٶ����͵�token�Լ���ţ�Ƶ����ø�Ϊͨ��config�����
3. adminģ��˵�ͨ����ǩʵʱ��ȡ
3. ����������js�ṹ����







