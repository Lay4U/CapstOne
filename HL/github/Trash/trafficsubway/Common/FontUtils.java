����   3 V
  4	  5	  6 7
  8	  9
 : ; <
 = > ?
 
 @
 
 A B
  C
  D
  E F 	fontUtils ,Lkr/go/seoul/trafficsubway/Common/FontUtils; 	mTypeface Landroid/graphics/Typeface; context Landroid/content/Context; <init> (Landroid/content/Context;)V Code LineNumberTable LocalVariableTable this getInstance G(Landroid/content/Context;)Lkr/go/seoul/trafficsubway/Common/FontUtils; StackMapTable getmTypeface ()Landroid/graphics/Typeface; setGlobalFont (Landroid/view/View;)V v Landroid/view/View; i I vg Landroid/view/ViewGroup; vgCnt view ? G 7 <clinit> ()V 
SourceFile FontUtils.java  1     *kr/go/seoul/trafficsubway/Common/FontUtils     H I J NotoSansCJKkr-DemiLight.otf K L M android/view/ViewGroup N O P Q android/widget/TextView R S T U # $ java/lang/Object android/view/View android/content/Context 	getAssets $()Landroid/content/res/AssetManager; android/graphics/Typeface createFromAsset Q(Landroid/content/res/AssetManager;Ljava/lang/String;)Landroid/graphics/Typeface; getChildCount ()I 
getChildAt (I)Landroid/view/View; setTypeface (Landroid/graphics/Typeface;)V setIncludeFontPadding (Z)V !      
     
     
             G     *� *W+� �              
                    	       k     '� � � Y*� � � � *� � 	� � �                  #         '            ! "     .     � �           !              # $     �     O+� M+� 
� F+� 
M,� >6� 3,� :� � � � � � � *� ���ͱ       2    %  &  '  (  )  * & + . , 9 - B / H ) N 3    >  & " % &   6 ' (   > ) *   9 + (    O       O , &       �  -� ) .�   / .    0 1           � �             2    3