# CircularProgressBar
## 自定义圆形进度View

写这篇文档是因为项目中刚好用到。

先看效果吧...

![device-2019-12-13-154946](img/device-2019-12-13-154946.gif)

<<<<<<< HEAD
=======


集成方式：

1. gradle

  `

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
`

`

	dependencies {
	        implementation 'com.github.Johnjson:CircularProgressBar:v1.0.0'
	}
`

2. maven

`

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
`

`

	<dependency>
	    <groupId>com.github.Johnjson</groupId>
	    <artifactId>CircularProgressBar</artifactId>
	    <version>v1.0.0</version>
	</dependency>
`

使用：

1. xml

`

```
<com.click.progress_library.view.CircularProgressBar
    android:id="@+id/CircularProgressBar2"
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:layout_below="@+id/CircularProgressBar1"
    android:layout_gravity="center_horizontal"
    android:layout_marginLeft="100dp"
    android:layout_marginTop="100dp"
    app:circular_cTextColor="#2395FF" //显示文字颜色
    app:circular_cTextSize="25dp" //显示文字大小
    app:circular_circleWidth="8dp" //显示圆进度边框大小
    app:circular_defaultColor="@color/colorGray"//显示圆进度边框默认颜色
    app:circular_runColor="@color/colorWhith"//显示圆进度边框进度颜色
    app:circular_directionof="true"//显示圆进度边框进度方向
    />
```

`

`

```
CircularProgressBar CircularProgressBar1 = (CircularProgressBar) findViewById(R.id.CircularProgressBar1);
CircularProgressBar1.setDuration(10 * 1000, new OnFinishListener() {
    @Override
    public void onFinish() {
        Toast.makeText(MainActivity.this, "完成", Toast.LENGTH_SHORT).show();
    }
});
```

`
>>>>>>> 添加使用方式
