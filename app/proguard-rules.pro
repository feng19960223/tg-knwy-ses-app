#############################################
# 对于一些基本指令的添加
#############################################
#代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5
#混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
 #指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
#指定不去忽略非公共库的类
-dontskipnonpubliclibraryclassmembers
#不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify
#shrinker（压缩）.声明不压缩输入文件。默认情况下，除了-keep相关配置指定的类，所有其它没有被引用到的类都会被删除。每次optimizate操作之后，也会执行一次压缩操作，因为每次optimizate操作可能删除一部分不再需要的类。
-dontshrink
#optimizer（优化）.声明不优化输入文件。默认情况下，优化选项是开启的，并且所有的优化都是在字节码层进行的。
-dontoptimize
# 混淆时是否记录日志
-verbose
 # 然后使用printmapping指定映射文件的名称
-printmapping priguardMapping.txt
-printusage unused.txt
#指定混淆是采用的算法，后面的参数是一个过滤器.这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保留Annotation不混淆
-keepattributes *Annotation*
# 避免混淆泛型
-keepattributes Signature
# 避免混淆内部类
-keepattributes InnerClasses
# 避免混淆异常
-keepattributes Exceptions
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
#我们不要使用-ignorewarnings语句，这个会忽略所有警告，会有很多潜在的风险。
#-ignorewarnings
#############################################
# Android开发中一些需要保留的公共部分
#############################################
#保留我们使用的四大组件，自定义的Application等等这些类不被混淆,因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.Fragment
#保留support下的所有类及其内部类
-keep class android.support.** {*;}
#保留R下面的资源
-keep class **.R$* {*;}
# 对R文件下的所有类及其方法，都不能被混淆
-keepclassmembers class **.R$* {*;}
#保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#保留在Activity中的方法参数是view的方法，这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
#保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#对于带有回调函数的onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
}
#webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
#############################################
# 第三方依赖库
#############################################
### lombok ###
-keep class lombok.** { *; }
-dontwarn lombok.**
### ButterKnife ###
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
### arouter ###
-keep class com.alibaba.** { *; }
-dontwarn com.alibaba.**
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
### fastjson ###
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
### gson ###
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}
-keep class sun.misc.Unsafe { *; }
### utilcode 工具包 ###
-keep class com.blankj.utilcode.** { *; }
-keepclassmembers class com.blankj.utilcode.** { *; }
-dontwarn com.blankj.utilcode.**
###  Retrolambda ###
-dontwarn java.lang.invoke.*
### okhttp ###
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
### glide ###
-keepnames class com.turingoal.bts.zypj.manager.GlideManger
#-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
### greenDAO 3 ###
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**
### picture ###
-keep class com.luck.picture.lib.** {*;}
### rxjava ###
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
###  支付宝 ###
#-libraryjars libs/alipaysdk.jar
#-dontwarn com.alipay.android.app.**
#-keep public class com.alipay.**  { *; }
###  EventBus ###
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
###  retroift ###
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
#极光推送
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
# other
-dontwarn org.springframework.**
-dontwarn android.util.FloatMath
#############################################
# 当前app
#############################################
-keep class com.turingoal.common.bean.**
-keep class com.turingoal.bjcorona.ses.bean.**
-keep public class * extends com.turingoal.bjcorona.ses.ui.adapter

