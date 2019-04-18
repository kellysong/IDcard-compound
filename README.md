# IDcard-compound
中华人民共和国身份证合成工具  
	1. 支持根据基本信息合成身份证正反面  
	2. 支持身份证正反面合成在一页
# 效果图
demo下载[app-debug.apk](app-debug.apk) 
<img src="https://github.com/kellysong/IDcard-compound/blob/master/screenshot/20190416105527.png" width="30%" alt="加载中..."/>
# 引用

##Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }

##Step 2. Add the dependency

	dependencies {
		        implementation 'com.github.kellysong:IDcard-compound:1.0.0'
		}

或者引用本地lib
	
	implementation project(':IDcard-compound-library')

# 使用
## 方法列表

方法 | 描述
---- | ----
configParams | 配置合成参数，缺省
compound | 合成，异步调用
release | 释放内存

## 使用步骤

### 1.初始化
	//构建身份证基本信息
	//头像可以是Bitmap或者原始头像整形数组
	IdentityCard identityCard = new IdentityCard(name, sex, nation, birthday, address, idCardNo,police, expiryDate, headImg);
	//初始化合成实例
	IdentityCardHandler identityCardHandler = IdentityCardHandler.getInstance(this);

### 2.合成调用

1. 根据基本信息合成身份证正反面


        identityCardHandler.compound(identityCard, new CompoundListener() {
            @Override
            public void onStart() {
 				// TODO 合成开始前调用，可以启动 loading UI
            }

            @Override
            public void onSuccess(IdentityCard identityCard) {
				// TODO 合成成功后调用，返回合成后的身份实体
                 Bitmap frontBitmap = identityCard.getFrontBitmap();//正面
                 Bitmap backBitmap = identityCard.getBackBitmap();//反面
     			 String frontImageBase64 = identityCard.getFrontImageBase64();//正面base64
                 String backImageBase64 = identityCard.getBackImageBase64();//反面base64

            }

            @Override
            public void onFailed(Throwable t) {
               // TODO 合成失败调用，用于排除问题
            }
        });
2. 身份证正反面合成在一页

        identityCardHandler.compound(true,identityCard, new CompoundListener() {
            @Override
            public void onStart() {
 				// TODO 合成开始前调用，可以启动 loading UI
            }

            @Override
            public void onSuccess(IdentityCard identityCard) {
				// TODO 合成成功后调用，返回合成后的身份实体
               	Bitmap fullBitmap = identityCard.getFullBitmap();//正反合并页
                String fullBitmapBase64 = identityCard.getFullBitmapBase64();//正反base64


            }

            @Override
            public void onFailed(Throwable t) {
               // TODO 合成失败调用，用于排除问题
            }
        });
### 3.释放内存

	identityCardHandler.release();

### 4.其它调用

	//对身份证进行缩放调整
	BitmapUtils.scaleBitmap(identityCard.getFrontBitmap(), 1.2f)

# License

    Copyright 2019 Song Jiali
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.