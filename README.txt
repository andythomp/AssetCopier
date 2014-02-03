AssetCopier

This package is designed to be included in an Android project, but it can be used as an example as well for how to include files from Assets onto the SDCard.

In order to include this package in an Android build, include it under packages/apps as an example starting point. The Android.mk will be included and the project will be built.

To include it to your specific device, locate the device-*.mk file for your device and add it to product packages. i.e.:

PRODUCT_PACKAGES += \
	AssetCopier

This will include it to your unique build.

To see how to include files, see the samples directory for an example. Essentially, files included in assets/sdcard will be added to the device's sdcard on boot in their respective paths. Be careful with how this is used, you could do damage with this application.
