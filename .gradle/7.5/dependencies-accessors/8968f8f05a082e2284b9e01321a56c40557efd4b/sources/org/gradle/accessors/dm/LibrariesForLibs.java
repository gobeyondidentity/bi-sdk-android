package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
*/
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AndroidLibraryAccessors laccForAndroidLibraryAccessors = new AndroidLibraryAccessors(owner);
    private final AndroidxLibraryAccessors laccForAndroidxLibraryAccessors = new AndroidxLibraryAccessors(owner);
    private final CodeLibraryAccessors laccForCodeLibraryAccessors = new CodeLibraryAccessors(owner);
    private final DeviceLibraryAccessors laccForDeviceLibraryAccessors = new DeviceLibraryAccessors(owner);
    private final DokkaLibraryAccessors laccForDokkaLibraryAccessors = new DokkaLibraryAccessors(owner);
    private final KotlinLibraryAccessors laccForKotlinLibraryAccessors = new KotlinLibraryAccessors(owner);
    private final KotlinxLibraryAccessors laccForKotlinxLibraryAccessors = new KotlinxLibraryAccessors(owner);
    private final KtlintLibraryAccessors laccForKtlintLibraryAccessors = new KtlintLibraryAccessors(owner);
    private final MockitoLibraryAccessors laccForMockitoLibraryAccessors = new MockitoLibraryAccessors(owner);
    private final OkhttpLibraryAccessors laccForOkhttpLibraryAccessors = new OkhttpLibraryAccessors(owner);
    private final OktaLibraryAccessors laccForOktaLibraryAccessors = new OktaLibraryAccessors(owner);
    private final RetrofitLibraryAccessors laccForRetrofitLibraryAccessors = new RetrofitLibraryAccessors(owner);
    private final ZxingLibraryAccessors laccForZxingLibraryAccessors = new ZxingLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(providers, config);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers) {
        super(config, providers);
    }

        /**
         * Creates a dependency provider for auth0 (com.auth0.android:auth0)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAuth0() { return create("auth0"); }

        /**
         * Creates a dependency provider for core (com.beyondidentity.android.sdk:core)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() { return create("core"); }

        /**
         * Creates a dependency provider for enclave (com.beyondidentity.android.sdk:enclave)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getEnclave() { return create("enclave"); }

        /**
         * Creates a dependency provider for gson (com.google.code.gson:gson)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGson() { return create("gson"); }

        /**
         * Creates a dependency provider for junit (junit:junit)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJunit() { return create("junit"); }

        /**
         * Creates a dependency provider for jwtdecode (com.auth0.android:jwtdecode)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJwtdecode() { return create("jwtdecode"); }

        /**
         * Creates a dependency provider for log (com.beyondidentity.android.sdk:log)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLog() { return create("log"); }

        /**
         * Creates a dependency provider for material (com.google.android.material:material)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMaterial() { return create("material"); }

        /**
         * Creates a dependency provider for optics (com.beyondidentity.android.sdk:optics)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getOptics() { return create("optics"); }

        /**
         * Creates a dependency provider for prelude (com.beyondidentity.android.sdk:prelude)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPrelude() { return create("prelude"); }

        /**
         * Creates a dependency provider for proto (com.beyondidentity.android.sdk:proto)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getProto() { return create("proto"); }

        /**
         * Creates a dependency provider for robolectric (org.robolectric:robolectric)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getRobolectric() { return create("robolectric"); }

        /**
         * Creates a dependency provider for timber (com.jakewharton.timber:timber)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTimber() { return create("timber"); }

    /**
     * Returns the group of libraries at android
     */
    public AndroidLibraryAccessors getAndroid() { return laccForAndroidLibraryAccessors; }

    /**
     * Returns the group of libraries at androidx
     */
    public AndroidxLibraryAccessors getAndroidx() { return laccForAndroidxLibraryAccessors; }

    /**
     * Returns the group of libraries at code
     */
    public CodeLibraryAccessors getCode() { return laccForCodeLibraryAccessors; }

    /**
     * Returns the group of libraries at device
     */
    public DeviceLibraryAccessors getDevice() { return laccForDeviceLibraryAccessors; }

    /**
     * Returns the group of libraries at dokka
     */
    public DokkaLibraryAccessors getDokka() { return laccForDokkaLibraryAccessors; }

    /**
     * Returns the group of libraries at kotlin
     */
    public KotlinLibraryAccessors getKotlin() { return laccForKotlinLibraryAccessors; }

    /**
     * Returns the group of libraries at kotlinx
     */
    public KotlinxLibraryAccessors getKotlinx() { return laccForKotlinxLibraryAccessors; }

    /**
     * Returns the group of libraries at ktlint
     */
    public KtlintLibraryAccessors getKtlint() { return laccForKtlintLibraryAccessors; }

    /**
     * Returns the group of libraries at mockito
     */
    public MockitoLibraryAccessors getMockito() { return laccForMockitoLibraryAccessors; }

    /**
     * Returns the group of libraries at okhttp
     */
    public OkhttpLibraryAccessors getOkhttp() { return laccForOkhttpLibraryAccessors; }

    /**
     * Returns the group of libraries at okta
     */
    public OktaLibraryAccessors getOkta() { return laccForOktaLibraryAccessors; }

    /**
     * Returns the group of libraries at retrofit
     */
    public RetrofitLibraryAccessors getRetrofit() { return laccForRetrofitLibraryAccessors; }

    /**
     * Returns the group of libraries at zxing
     */
    public ZxingLibraryAccessors getZxing() { return laccForZxingLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() { return paccForPluginAccessors; }

    public static class AndroidLibraryAccessors extends SubDependencyFactory {
        private final AndroidGradleLibraryAccessors laccForAndroidGradleLibraryAccessors = new AndroidGradleLibraryAccessors(owner);

        public AndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at android.gradle
         */
        public AndroidGradleLibraryAccessors getGradle() { return laccForAndroidGradleLibraryAccessors; }

    }

    public static class AndroidGradleLibraryAccessors extends SubDependencyFactory {

        public AndroidGradleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for plugin (com.android.tools.build:gradle)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPlugin() { return create("android.gradle.plugin"); }

    }

    public static class AndroidxLibraryAccessors extends SubDependencyFactory {
        private final AndroidxActivityLibraryAccessors laccForAndroidxActivityLibraryAccessors = new AndroidxActivityLibraryAccessors(owner);
        private final AndroidxComposeLibraryAccessors laccForAndroidxComposeLibraryAccessors = new AndroidxComposeLibraryAccessors(owner);
        private final AndroidxCoreLibraryAccessors laccForAndroidxCoreLibraryAccessors = new AndroidxCoreLibraryAccessors(owner);
        private final AndroidxLifecycleLibraryAccessors laccForAndroidxLifecycleLibraryAccessors = new AndroidxLifecycleLibraryAccessors(owner);
        private final AndroidxPreferenceLibraryAccessors laccForAndroidxPreferenceLibraryAccessors = new AndroidxPreferenceLibraryAccessors(owner);
        private final AndroidxTestLibraryAccessors laccForAndroidxTestLibraryAccessors = new AndroidxTestLibraryAccessors(owner);

        public AndroidxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for appcompat (androidx.appcompat:appcompat)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAppcompat() { return create("androidx.appcompat"); }

            /**
             * Creates a dependency provider for biometric (androidx.biometric:biometric)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getBiometric() { return create("androidx.biometric"); }

            /**
             * Creates a dependency provider for browser (androidx.browser:browser)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getBrowser() { return create("androidx.browser"); }

            /**
             * Creates a dependency provider for constraintlayout (androidx.constraintlayout:constraintlayout)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getConstraintlayout() { return create("androidx.constraintlayout"); }

        /**
         * Returns the group of libraries at androidx.activity
         */
        public AndroidxActivityLibraryAccessors getActivity() { return laccForAndroidxActivityLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.compose
         */
        public AndroidxComposeLibraryAccessors getCompose() { return laccForAndroidxComposeLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.core
         */
        public AndroidxCoreLibraryAccessors getCore() { return laccForAndroidxCoreLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.lifecycle
         */
        public AndroidxLifecycleLibraryAccessors getLifecycle() { return laccForAndroidxLifecycleLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.preference
         */
        public AndroidxPreferenceLibraryAccessors getPreference() { return laccForAndroidxPreferenceLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.test
         */
        public AndroidxTestLibraryAccessors getTest() { return laccForAndroidxTestLibraryAccessors; }

    }

    public static class AndroidxActivityLibraryAccessors extends SubDependencyFactory {

        public AndroidxActivityLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compose (androidx.activity:activity-compose)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCompose() { return create("androidx.activity.compose"); }

    }

    public static class AndroidxComposeLibraryAccessors extends SubDependencyFactory {
        private final AndroidxComposeUiLibraryAccessors laccForAndroidxComposeUiLibraryAccessors = new AndroidxComposeUiLibraryAccessors(owner);

        public AndroidxComposeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for material (androidx.compose.material:material)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMaterial() { return create("androidx.compose.material"); }

        /**
         * Returns the group of libraries at androidx.compose.ui
         */
        public AndroidxComposeUiLibraryAccessors getUi() { return laccForAndroidxComposeUiLibraryAccessors; }

    }

    public static class AndroidxComposeUiLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final AndroidxComposeUiTestLibraryAccessors laccForAndroidxComposeUiTestLibraryAccessors = new AndroidxComposeUiTestLibraryAccessors(owner);
        private final AndroidxComposeUiToolingLibraryAccessors laccForAndroidxComposeUiToolingLibraryAccessors = new AndroidxComposeUiToolingLibraryAccessors(owner);

        public AndroidxComposeUiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ui (androidx.compose.ui:ui)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.compose.ui"); }

        /**
         * Returns the group of libraries at androidx.compose.ui.test
         */
        public AndroidxComposeUiTestLibraryAccessors getTest() { return laccForAndroidxComposeUiTestLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.compose.ui.tooling
         */
        public AndroidxComposeUiToolingLibraryAccessors getTooling() { return laccForAndroidxComposeUiToolingLibraryAccessors; }

    }

    public static class AndroidxComposeUiTestLibraryAccessors extends SubDependencyFactory {

        public AndroidxComposeUiTestLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for junit4 (androidx.compose.ui:ui-test-junit4)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJunit4() { return create("androidx.compose.ui.test.junit4"); }

            /**
             * Creates a dependency provider for manifest (androidx.compose.ui:ui-test-manifest)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getManifest() { return create("androidx.compose.ui.test.manifest"); }

    }

    public static class AndroidxComposeUiToolingLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxComposeUiToolingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for tooling (androidx.compose.ui:ui-tooling)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.compose.ui.tooling"); }

            /**
             * Creates a dependency provider for preview (androidx.compose.ui:ui-tooling-preview)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPreview() { return create("androidx.compose.ui.tooling.preview"); }

    }

    public static class AndroidxCoreLibraryAccessors extends SubDependencyFactory {

        public AndroidxCoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.core:core-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.core.ktx"); }

    }

    public static class AndroidxLifecycleLibraryAccessors extends SubDependencyFactory {
        private final AndroidxLifecycleRuntimeLibraryAccessors laccForAndroidxLifecycleRuntimeLibraryAccessors = new AndroidxLifecycleRuntimeLibraryAccessors(owner);
        private final AndroidxLifecycleViewmodelLibraryAccessors laccForAndroidxLifecycleViewmodelLibraryAccessors = new AndroidxLifecycleViewmodelLibraryAccessors(owner);

        public AndroidxLifecycleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for common (androidx.lifecycle:lifecycle-common)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCommon() { return create("androidx.lifecycle.common"); }

        /**
         * Returns the group of libraries at androidx.lifecycle.runtime
         */
        public AndroidxLifecycleRuntimeLibraryAccessors getRuntime() { return laccForAndroidxLifecycleRuntimeLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.lifecycle.viewmodel
         */
        public AndroidxLifecycleViewmodelLibraryAccessors getViewmodel() { return laccForAndroidxLifecycleViewmodelLibraryAccessors; }

    }

    public static class AndroidxLifecycleRuntimeLibraryAccessors extends SubDependencyFactory {

        public AndroidxLifecycleRuntimeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.lifecycle:lifecycle-runtime-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.lifecycle.runtime.ktx"); }

    }

    public static class AndroidxLifecycleViewmodelLibraryAccessors extends SubDependencyFactory {

        public AndroidxLifecycleViewmodelLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compose (androidx.lifecycle:lifecycle-viewmodel-compose)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCompose() { return create("androidx.lifecycle.viewmodel.compose"); }

            /**
             * Creates a dependency provider for ktx (androidx.lifecycle:lifecycle-viewmodel-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.lifecycle.viewmodel.ktx"); }

            /**
             * Creates a dependency provider for savedstate (androidx.lifecycle:lifecycle-viewmodel-savedstate)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getSavedstate() { return create("androidx.lifecycle.viewmodel.savedstate"); }

    }

    public static class AndroidxPreferenceLibraryAccessors extends SubDependencyFactory {

        public AndroidxPreferenceLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.preference:preference-ktx)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.preference.ktx"); }

    }

    public static class AndroidxTestLibraryAccessors extends SubDependencyFactory {
        private final AndroidxTestEspressoLibraryAccessors laccForAndroidxTestEspressoLibraryAccessors = new AndroidxTestEspressoLibraryAccessors(owner);
        private final AndroidxTestExtLibraryAccessors laccForAndroidxTestExtLibraryAccessors = new AndroidxTestExtLibraryAccessors(owner);

        public AndroidxTestLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.test:core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("androidx.test.core"); }

            /**
             * Creates a dependency provider for rules (androidx.test:rules)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getRules() { return create("androidx.test.rules"); }

            /**
             * Creates a dependency provider for runner (androidx.test:runner)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getRunner() { return create("androidx.test.runner"); }

        /**
         * Returns the group of libraries at androidx.test.espresso
         */
        public AndroidxTestEspressoLibraryAccessors getEspresso() { return laccForAndroidxTestEspressoLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.test.ext
         */
        public AndroidxTestExtLibraryAccessors getExt() { return laccForAndroidxTestExtLibraryAccessors; }

    }

    public static class AndroidxTestEspressoLibraryAccessors extends SubDependencyFactory {

        public AndroidxTestEspressoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.test.espresso:espresso-core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("androidx.test.espresso.core"); }

    }

    public static class AndroidxTestExtLibraryAccessors extends SubDependencyFactory {

        public AndroidxTestExtLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for junit (androidx.test.ext:junit)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJunit() { return create("androidx.test.ext.junit"); }

    }

    public static class CodeLibraryAccessors extends SubDependencyFactory {

        public CodeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for scanner (com.github.yuriy-budiyev:code-scanner)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getScanner() { return create("code.scanner"); }

    }

    public static class DeviceLibraryAccessors extends SubDependencyFactory {

        public DeviceLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for info (com.beyondidentity.android.sdk:device-info)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getInfo() { return create("device.info"); }

    }

    public static class DokkaLibraryAccessors extends SubDependencyFactory {
        private final DokkaGradleLibraryAccessors laccForDokkaGradleLibraryAccessors = new DokkaGradleLibraryAccessors(owner);

        public DokkaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at dokka.gradle
         */
        public DokkaGradleLibraryAccessors getGradle() { return laccForDokkaGradleLibraryAccessors; }

    }

    public static class DokkaGradleLibraryAccessors extends SubDependencyFactory {

        public DokkaGradleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for plugin (org.jetbrains.dokka:dokka-gradle-plugin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPlugin() { return create("dokka.gradle.plugin"); }

    }

    public static class KotlinLibraryAccessors extends SubDependencyFactory {
        private final KotlinGradleLibraryAccessors laccForKotlinGradleLibraryAccessors = new KotlinGradleLibraryAccessors(owner);
        private final KotlinSerializationLibraryAccessors laccForKotlinSerializationLibraryAccessors = new KotlinSerializationLibraryAccessors(owner);
        private final KotlinStdlibLibraryAccessors laccForKotlinStdlibLibraryAccessors = new KotlinStdlibLibraryAccessors(owner);

        public KotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for reflect (org.jetbrains.kotlin:kotlin-reflect)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getReflect() { return create("kotlin.reflect"); }

        /**
         * Returns the group of libraries at kotlin.gradle
         */
        public KotlinGradleLibraryAccessors getGradle() { return laccForKotlinGradleLibraryAccessors; }

        /**
         * Returns the group of libraries at kotlin.serialization
         */
        public KotlinSerializationLibraryAccessors getSerialization() { return laccForKotlinSerializationLibraryAccessors; }

        /**
         * Returns the group of libraries at kotlin.stdlib
         */
        public KotlinStdlibLibraryAccessors getStdlib() { return laccForKotlinStdlibLibraryAccessors; }

    }

    public static class KotlinGradleLibraryAccessors extends SubDependencyFactory {

        public KotlinGradleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for plugin (org.jetbrains.kotlin:kotlin-gradle-plugin)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPlugin() { return create("kotlin.gradle.plugin"); }

    }

    public static class KotlinSerializationLibraryAccessors extends SubDependencyFactory {

        public KotlinSerializationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for plugin (org.jetbrains.kotlin:kotlin-serialization)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPlugin() { return create("kotlin.serialization.plugin"); }

    }

    public static class KotlinStdlibLibraryAccessors extends SubDependencyFactory {

        public KotlinStdlibLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jdk8 (org.jetbrains.kotlin:kotlin-stdlib-jdk8)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJdk8() { return create("kotlin.stdlib.jdk8"); }

    }

    public static class KotlinxLibraryAccessors extends SubDependencyFactory {
        private final KotlinxCoroutinesLibraryAccessors laccForKotlinxCoroutinesLibraryAccessors = new KotlinxCoroutinesLibraryAccessors(owner);
        private final KotlinxSerializationLibraryAccessors laccForKotlinxSerializationLibraryAccessors = new KotlinxSerializationLibraryAccessors(owner);

        public KotlinxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at kotlinx.coroutines
         */
        public KotlinxCoroutinesLibraryAccessors getCoroutines() { return laccForKotlinxCoroutinesLibraryAccessors; }

        /**
         * Returns the group of libraries at kotlinx.serialization
         */
        public KotlinxSerializationLibraryAccessors getSerialization() { return laccForKotlinxSerializationLibraryAccessors; }

    }

    public static class KotlinxCoroutinesLibraryAccessors extends SubDependencyFactory {

        public KotlinxCoroutinesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (org.jetbrains.kotlinx:kotlinx-coroutines-android)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("kotlinx.coroutines.android"); }

            /**
             * Creates a dependency provider for test (org.jetbrains.kotlinx:kotlinx-coroutines-test)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTest() { return create("kotlinx.coroutines.test"); }

    }

    public static class KotlinxSerializationLibraryAccessors extends SubDependencyFactory {

        public KotlinxSerializationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for json (org.jetbrains.kotlinx:kotlinx-serialization-json)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJson() { return create("kotlinx.serialization.json"); }

    }

    public static class KtlintLibraryAccessors extends SubDependencyFactory {
        private final KtlintGradleLibraryAccessors laccForKtlintGradleLibraryAccessors = new KtlintGradleLibraryAccessors(owner);

        public KtlintLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at ktlint.gradle
         */
        public KtlintGradleLibraryAccessors getGradle() { return laccForKtlintGradleLibraryAccessors; }

    }

    public static class KtlintGradleLibraryAccessors extends SubDependencyFactory {

        public KtlintGradleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for plugin (org.jlleitschuh.gradle:ktlint-gradle)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getPlugin() { return create("ktlint.gradle.plugin"); }

    }

    public static class MockitoLibraryAccessors extends SubDependencyFactory {

        public MockitoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (org.mockito:mockito-android)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("mockito.android"); }

            /**
             * Creates a dependency provider for core (org.mockito:mockito-core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("mockito.core"); }

    }

    public static class OkhttpLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final OkhttpLoggingLibraryAccessors laccForOkhttpLoggingLibraryAccessors = new OkhttpLoggingLibraryAccessors(owner);

        public OkhttpLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for okhttp (com.squareup.okhttp3:okhttp)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("okhttp"); }

        /**
         * Returns the group of libraries at okhttp.logging
         */
        public OkhttpLoggingLibraryAccessors getLogging() { return laccForOkhttpLoggingLibraryAccessors; }

    }

    public static class OkhttpLoggingLibraryAccessors extends SubDependencyFactory {

        public OkhttpLoggingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for interceptor (com.squareup.okhttp3:logging-interceptor)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getInterceptor() { return create("okhttp.logging.interceptor"); }

    }

    public static class OktaLibraryAccessors extends SubDependencyFactory {
        private final OktaIdxLibraryAccessors laccForOktaIdxLibraryAccessors = new OktaIdxLibraryAccessors(owner);
        private final OktaJwtLibraryAccessors laccForOktaJwtLibraryAccessors = new OktaJwtLibraryAccessors(owner);
        private final OktaOidcLibraryAccessors laccForOktaOidcLibraryAccessors = new OktaOidcLibraryAccessors(owner);

        public OktaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at okta.idx
         */
        public OktaIdxLibraryAccessors getIdx() { return laccForOktaIdxLibraryAccessors; }

        /**
         * Returns the group of libraries at okta.jwt
         */
        public OktaJwtLibraryAccessors getJwt() { return laccForOktaJwtLibraryAccessors; }

        /**
         * Returns the group of libraries at okta.oidc
         */
        public OktaOidcLibraryAccessors getOidc() { return laccForOktaOidcLibraryAccessors; }

    }

    public static class OktaIdxLibraryAccessors extends SubDependencyFactory {
        private final OktaIdxJavaLibraryAccessors laccForOktaIdxJavaLibraryAccessors = new OktaIdxJavaLibraryAccessors(owner);

        public OktaIdxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at okta.idx.java
         */
        public OktaIdxJavaLibraryAccessors getJava() { return laccForOktaIdxJavaLibraryAccessors; }

    }

    public static class OktaIdxJavaLibraryAccessors extends SubDependencyFactory {

        public OktaIdxJavaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (com.okta.idx.sdk:okta-idx-java-api)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() { return create("okta.idx.java.api"); }

    }

    public static class OktaJwtLibraryAccessors extends SubDependencyFactory {
        private final OktaJwtVerifierLibraryAccessors laccForOktaJwtVerifierLibraryAccessors = new OktaJwtVerifierLibraryAccessors(owner);

        public OktaJwtLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at okta.jwt.verifier
         */
        public OktaJwtVerifierLibraryAccessors getVerifier() { return laccForOktaJwtVerifierLibraryAccessors; }

    }

    public static class OktaJwtVerifierLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public OktaJwtVerifierLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for verifier (com.okta.jwt:okta-jwt-verifier)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("okta.jwt.verifier"); }

            /**
             * Creates a dependency provider for impl (com.okta.jwt:okta-jwt-verifier-impl)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getImpl() { return create("okta.jwt.verifier.impl"); }

    }

    public static class OktaOidcLibraryAccessors extends SubDependencyFactory {

        public OktaOidcLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (com.okta.android:okta-oidc-android)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("okta.oidc.android"); }

    }

    public static class RetrofitLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final RetrofitConverterLibraryAccessors laccForRetrofitConverterLibraryAccessors = new RetrofitConverterLibraryAccessors(owner);

        public RetrofitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for retrofit (com.squareup.retrofit2:retrofit)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("retrofit"); }

        /**
         * Returns the group of libraries at retrofit.converter
         */
        public RetrofitConverterLibraryAccessors getConverter() { return laccForRetrofitConverterLibraryAccessors; }

    }

    public static class RetrofitConverterLibraryAccessors extends SubDependencyFactory {

        public RetrofitConverterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gson (com.squareup.retrofit2:converter-gson)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGson() { return create("retrofit.converter.gson"); }

    }

    public static class ZxingLibraryAccessors extends SubDependencyFactory {
        private final ZxingAndroidLibraryAccessors laccForZxingAndroidLibraryAccessors = new ZxingAndroidLibraryAccessors(owner);

        public ZxingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at zxing.android
         */
        public ZxingAndroidLibraryAccessors getAndroid() { return laccForZxingAndroidLibraryAccessors; }

    }

    public static class ZxingAndroidLibraryAccessors extends SubDependencyFactory {

        public ZxingAndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for embedded (com.journeyapps:zxing-android-embedded)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getEmbedded() { return create("zxing.android.embedded"); }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final AndroidVersionAccessors vaccForAndroidVersionAccessors = new AndroidVersionAccessors(providers, config);
        private final AndroidxVersionAccessors vaccForAndroidxVersionAccessors = new AndroidxVersionAccessors(providers, config);
        private final CodeVersionAccessors vaccForCodeVersionAccessors = new CodeVersionAccessors(providers, config);
        private final KotlinxVersionAccessors vaccForKotlinxVersionAccessors = new KotlinxVersionAccessors(providers, config);
        private final MavenVersionAccessors vaccForMavenVersionAccessors = new MavenVersionAccessors(providers, config);
        private final OktaVersionAccessors vaccForOktaVersionAccessors = new OktaVersionAccessors(providers, config);
        private final ZxingVersionAccessors vaccForZxingVersionAccessors = new ZxingVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: auth0 (2.8.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAuth0() { return getVersion("auth0"); }

            /**
             * Returns the version associated to this alias: dokka (1.7.20)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getDokka() { return getVersion("dokka"); }

            /**
             * Returns the version associated to this alias: gson (2.10.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getGson() { return getVersion("gson"); }

            /**
             * Returns the version associated to this alias: junit (4.13.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJunit() { return getVersion("junit"); }

            /**
             * Returns the version associated to this alias: jwtdecode (2.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJwtdecode() { return getVersion("jwtdecode"); }

            /**
             * Returns the version associated to this alias: kotlin (1.8.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getKotlin() { return getVersion("kotlin"); }

            /**
             * Returns the version associated to this alias: ktlint (11.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getKtlint() { return getVersion("ktlint"); }

            /**
             * Returns the version associated to this alias: material (1.8.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMaterial() { return getVersion("material"); }

            /**
             * Returns the version associated to this alias: mockito (3.6.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMockito() { return getVersion("mockito"); }

            /**
             * Returns the version associated to this alias: okhttp (4.9.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getOkhttp() { return getVersion("okhttp"); }

            /**
             * Returns the version associated to this alias: retrofit (2.7.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getRetrofit() { return getVersion("retrofit"); }

            /**
             * Returns the version associated to this alias: roboelectric (4.6.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getRoboelectric() { return getVersion("roboelectric"); }

            /**
             * Returns the version associated to this alias: timber (4.7.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTimber() { return getVersion("timber"); }

        /**
         * Returns the group of versions at versions.android
         */
        public AndroidVersionAccessors getAndroid() { return vaccForAndroidVersionAccessors; }

        /**
         * Returns the group of versions at versions.androidx
         */
        public AndroidxVersionAccessors getAndroidx() { return vaccForAndroidxVersionAccessors; }

        /**
         * Returns the group of versions at versions.code
         */
        public CodeVersionAccessors getCode() { return vaccForCodeVersionAccessors; }

        /**
         * Returns the group of versions at versions.kotlinx
         */
        public KotlinxVersionAccessors getKotlinx() { return vaccForKotlinxVersionAccessors; }

        /**
         * Returns the group of versions at versions.maven
         */
        public MavenVersionAccessors getMaven() { return vaccForMavenVersionAccessors; }

        /**
         * Returns the group of versions at versions.okta
         */
        public OktaVersionAccessors getOkta() { return vaccForOktaVersionAccessors; }

        /**
         * Returns the group of versions at versions.zxing
         */
        public ZxingVersionAccessors getZxing() { return vaccForZxingVersionAccessors; }

    }

    public static class AndroidVersionAccessors extends VersionFactory  {

        private final AndroidGradleVersionAccessors vaccForAndroidGradleVersionAccessors = new AndroidGradleVersionAccessors(providers, config);
        public AndroidVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.android.gradle
         */
        public AndroidGradleVersionAccessors getGradle() { return vaccForAndroidGradleVersionAccessors; }

    }

    public static class AndroidGradleVersionAccessors extends VersionFactory  {

        public AndroidGradleVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: android.gradle.plugin (7.4.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPlugin() { return getVersion("android.gradle.plugin"); }

    }

    public static class AndroidxVersionAccessors extends VersionFactory  {

        private final AndroidxComposeVersionAccessors vaccForAndroidxComposeVersionAccessors = new AndroidxComposeVersionAccessors(providers, config);
        private final AndroidxTestVersionAccessors vaccForAndroidxTestVersionAccessors = new AndroidxTestVersionAccessors(providers, config);
        public AndroidxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: androidx.activity (1.6.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getActivity() { return getVersion("androidx.activity"); }

            /**
             * Returns the version associated to this alias: androidx.appcompat (1.4.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAppcompat() { return getVersion("androidx.appcompat"); }

            /**
             * Returns the version associated to this alias: androidx.biometric (1.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getBiometric() { return getVersion("androidx.biometric"); }

            /**
             * Returns the version associated to this alias: androidx.browser (1.4.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getBrowser() { return getVersion("androidx.browser"); }

            /**
             * Returns the version associated to this alias: androidx.constraintlayout (2.1.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getConstraintlayout() { return getVersion("androidx.constraintlayout"); }

            /**
             * Returns the version associated to this alias: androidx.core (1.9.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCore() { return getVersion("androidx.core"); }

            /**
             * Returns the version associated to this alias: androidx.lifecycle (2.5.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLifecycle() { return getVersion("androidx.lifecycle"); }

            /**
             * Returns the version associated to this alias: androidx.preference (1.2.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPreference() { return getVersion("androidx.preference"); }

        /**
         * Returns the group of versions at versions.androidx.compose
         */
        public AndroidxComposeVersionAccessors getCompose() { return vaccForAndroidxComposeVersionAccessors; }

        /**
         * Returns the group of versions at versions.androidx.test
         */
        public AndroidxTestVersionAccessors getTest() { return vaccForAndroidxTestVersionAccessors; }

    }

    public static class AndroidxComposeVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public AndroidxComposeVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the version associated to this alias: androidx.compose (1.4.0)
         * If the version is a rich version and that its not expressible as a
         * single version string, then an empty string is returned.
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("androidx.compose"); }

            /**
             * Returns the version associated to this alias: androidx.compose.compiler (1.4.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCompiler() { return getVersion("androidx.compose.compiler"); }

    }

    public static class AndroidxTestVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        private final AndroidxTestEspressoVersionAccessors vaccForAndroidxTestEspressoVersionAccessors = new AndroidxTestEspressoVersionAccessors(providers, config);
        private final AndroidxTestExtVersionAccessors vaccForAndroidxTestExtVersionAccessors = new AndroidxTestExtVersionAccessors(providers, config);
        public AndroidxTestVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the version associated to this alias: androidx.test (1.5.0)
         * If the version is a rich version and that its not expressible as a
         * single version string, then an empty string is returned.
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("androidx.test"); }

        /**
         * Returns the group of versions at versions.androidx.test.espresso
         */
        public AndroidxTestEspressoVersionAccessors getEspresso() { return vaccForAndroidxTestEspressoVersionAccessors; }

        /**
         * Returns the group of versions at versions.androidx.test.ext
         */
        public AndroidxTestExtVersionAccessors getExt() { return vaccForAndroidxTestExtVersionAccessors; }

    }

    public static class AndroidxTestEspressoVersionAccessors extends VersionFactory  {

        public AndroidxTestEspressoVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: androidx.test.espresso.core (3.5.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCore() { return getVersion("androidx.test.espresso.core"); }

    }

    public static class AndroidxTestExtVersionAccessors extends VersionFactory  {

        public AndroidxTestExtVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: androidx.test.ext.junit (1.1.5)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJunit() { return getVersion("androidx.test.ext.junit"); }

    }

    public static class CodeVersionAccessors extends VersionFactory  {

        public CodeVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: code.scanner (2.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getScanner() { return getVersion("code.scanner"); }

    }

    public static class KotlinxVersionAccessors extends VersionFactory  {

        public KotlinxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: kotlinx.coroutines (1.6.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCoroutines() { return getVersion("kotlinx.coroutines"); }

            /**
             * Returns the version associated to this alias: kotlinx.serialization (1.4.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSerialization() { return getVersion("kotlinx.serialization"); }

    }

    public static class MavenVersionAccessors extends VersionFactory  {

        public MavenVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: maven.publish (+)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPublish() { return getVersion("maven.publish"); }

    }

    public static class OktaVersionAccessors extends VersionFactory  {

        private final OktaIdxVersionAccessors vaccForOktaIdxVersionAccessors = new OktaIdxVersionAccessors(providers, config);
        private final OktaJwtVersionAccessors vaccForOktaJwtVersionAccessors = new OktaJwtVersionAccessors(providers, config);
        private final OktaOidcVersionAccessors vaccForOktaOidcVersionAccessors = new OktaOidcVersionAccessors(providers, config);
        public OktaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.okta.idx
         */
        public OktaIdxVersionAccessors getIdx() { return vaccForOktaIdxVersionAccessors; }

        /**
         * Returns the group of versions at versions.okta.jwt
         */
        public OktaJwtVersionAccessors getJwt() { return vaccForOktaJwtVersionAccessors; }

        /**
         * Returns the group of versions at versions.okta.oidc
         */
        public OktaOidcVersionAccessors getOidc() { return vaccForOktaOidcVersionAccessors; }

    }

    public static class OktaIdxVersionAccessors extends VersionFactory  {

        private final OktaIdxJavaVersionAccessors vaccForOktaIdxJavaVersionAccessors = new OktaIdxJavaVersionAccessors(providers, config);
        public OktaIdxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.okta.idx.java
         */
        public OktaIdxJavaVersionAccessors getJava() { return vaccForOktaIdxJavaVersionAccessors; }

    }

    public static class OktaIdxJavaVersionAccessors extends VersionFactory  {

        public OktaIdxJavaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: okta.idx.java.api (3.0.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getApi() { return getVersion("okta.idx.java.api"); }

    }

    public static class OktaJwtVersionAccessors extends VersionFactory  {

        public OktaJwtVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: okta.jwt.verifier (0.5.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getVerifier() { return getVersion("okta.jwt.verifier"); }

    }

    public static class OktaOidcVersionAccessors extends VersionFactory  {

        public OktaOidcVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: okta.oidc.android (1.3.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAndroid() { return getVersion("okta.oidc.android"); }

    }

    public static class ZxingVersionAccessors extends VersionFactory  {

        private final ZxingAndroidVersionAccessors vaccForZxingAndroidVersionAccessors = new ZxingAndroidVersionAccessors(providers, config);
        public ZxingVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of versions at versions.zxing.android
         */
        public ZxingAndroidVersionAccessors getAndroid() { return vaccForZxingAndroidVersionAccessors; }

    }

    public static class ZxingAndroidVersionAccessors extends VersionFactory  {

        public ZxingAndroidVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: zxing.android.embedded (4.3.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getEmbedded() { return getVersion("zxing.android.embedded"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

    public static class PluginAccessors extends PluginFactory {
        private final ComPluginAccessors baccForComPluginAccessors = new ComPluginAccessors(providers, config);
        private final KotlinPluginAccessors baccForKotlinPluginAccessors = new KotlinPluginAccessors(providers, config);
        private final KotlinxPluginAccessors baccForKotlinxPluginAccessors = new KotlinxPluginAccessors(providers, config);
        private final MavenPluginAccessors baccForMavenPluginAccessors = new MavenPluginAccessors(providers, config);
        private final OrgPluginAccessors baccForOrgPluginAccessors = new OrgPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of bundles at plugins.com
         */
        public ComPluginAccessors getCom() { return baccForComPluginAccessors; }

        /**
         * Returns the group of bundles at plugins.kotlin
         */
        public KotlinPluginAccessors getKotlin() { return baccForKotlinPluginAccessors; }

        /**
         * Returns the group of bundles at plugins.kotlinx
         */
        public KotlinxPluginAccessors getKotlinx() { return baccForKotlinxPluginAccessors; }

        /**
         * Returns the group of bundles at plugins.maven
         */
        public MavenPluginAccessors getMaven() { return baccForMavenPluginAccessors; }

        /**
         * Returns the group of bundles at plugins.org
         */
        public OrgPluginAccessors getOrg() { return baccForOrgPluginAccessors; }

    }

    public static class ComPluginAccessors extends PluginFactory {
        private final ComAndroidPluginAccessors baccForComAndroidPluginAccessors = new ComAndroidPluginAccessors(providers, config);

        public ComPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of bundles at plugins.com.android
         */
        public ComAndroidPluginAccessors getAndroid() { return baccForComAndroidPluginAccessors; }

    }

    public static class ComAndroidPluginAccessors extends PluginFactory {

        public ComAndroidPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for com.android.application to the plugin id 'com.android.application'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getApplication() { return createPlugin("com.android.application"); }

            /**
             * Creates a plugin provider for com.android.library to the plugin id 'com.android.library'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getLibrary() { return createPlugin("com.android.library"); }

    }

    public static class KotlinPluginAccessors extends PluginFactory {

        public KotlinPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for kotlin.android to the plugin id 'kotlin-android'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getAndroid() { return createPlugin("kotlin.android"); }

            /**
             * Creates a plugin provider for kotlin.parcelize to the plugin id 'kotlin-parcelize'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getParcelize() { return createPlugin("kotlin.parcelize"); }

    }

    public static class KotlinxPluginAccessors extends PluginFactory {

        public KotlinxPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for kotlinx.serialization to the plugin id 'kotlinx-serialization'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getSerialization() { return createPlugin("kotlinx.serialization"); }

    }

    public static class MavenPluginAccessors extends PluginFactory {

        public MavenPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for maven.publish to the plugin id 'maven-publish'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getPublish() { return createPlugin("maven.publish"); }

    }

    public static class OrgPluginAccessors extends PluginFactory {
        private final OrgJetbrainsPluginAccessors baccForOrgJetbrainsPluginAccessors = new OrgJetbrainsPluginAccessors(providers, config);
        private final OrgJlleitschuhPluginAccessors baccForOrgJlleitschuhPluginAccessors = new OrgJlleitschuhPluginAccessors(providers, config);

        public OrgPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of bundles at plugins.org.jetbrains
         */
        public OrgJetbrainsPluginAccessors getJetbrains() { return baccForOrgJetbrainsPluginAccessors; }

        /**
         * Returns the group of bundles at plugins.org.jlleitschuh
         */
        public OrgJlleitschuhPluginAccessors getJlleitschuh() { return baccForOrgJlleitschuhPluginAccessors; }

    }

    public static class OrgJetbrainsPluginAccessors extends PluginFactory {

        public OrgJetbrainsPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for org.jetbrains.dokka to the plugin id 'org.jetbrains.dokka'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getDokka() { return createPlugin("org.jetbrains.dokka"); }

    }

    public static class OrgJlleitschuhPluginAccessors extends PluginFactory {
        private final OrgJlleitschuhGradlePluginAccessors baccForOrgJlleitschuhGradlePluginAccessors = new OrgJlleitschuhGradlePluginAccessors(providers, config);

        public OrgJlleitschuhPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of bundles at plugins.org.jlleitschuh.gradle
         */
        public OrgJlleitschuhGradlePluginAccessors getGradle() { return baccForOrgJlleitschuhGradlePluginAccessors; }

    }

    public static class OrgJlleitschuhGradlePluginAccessors extends PluginFactory {

        public OrgJlleitschuhGradlePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for org.jlleitschuh.gradle.ktlint to the plugin id 'org.jlleitschuh.gradle.ktlint'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getKtlint() { return createPlugin("org.jlleitschuh.gradle.ktlint"); }

    }

}
