package com.retro.musicplayer.di;

import android.content.Context;
import com.retro.musicplayer.service.scanner.MusicScanner;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class ServiceModule_ProvideMusicScannerFactory implements Factory<MusicScanner> {
  private final Provider<Context> contextProvider;

  public ServiceModule_ProvideMusicScannerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MusicScanner get() {
    return provideMusicScanner(contextProvider.get());
  }

  public static ServiceModule_ProvideMusicScannerFactory create(Provider<Context> contextProvider) {
    return new ServiceModule_ProvideMusicScannerFactory(contextProvider);
  }

  public static MusicScanner provideMusicScanner(Context context) {
    return Preconditions.checkNotNullFromProvides(ServiceModule.INSTANCE.provideMusicScanner(context));
  }
}
