Pod::Spec.new do |s|
  s.name         = "RNKommunicateChat"
  s.version      = "1.0.0"
  s.summary      = "React Native SDK for Kommunicate customer support live chat"
  s.description  = <<-DESC
                  RNKommunicateChat
                   DESC
  s.homepage     = "https://kommunicate.io"
  s.license      = "BSD-3"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "codemonk@kommunicate.io" }
  s.platform     = :ios, "13.0"
  s.source       = { :git => "https://github.com/Kommunicate-io/Kommunicate-React-Native-SDK", :tag => "master" }
  s.source_files  = "ios/*.{h,m,swift}"
  s.requires_arc = true
  s.dependency 'React'

  # The Kommunicate iOS SDK is distributed via Swift Package Manager (CocoaPods trunk goes read-only,
  # last pod release is 7.3.6). React Native 0.75+ provides `spm_dependency`, which pulls the package in
  # while the app keeps using `pod install`. Older RN versions fall back to the last CocoaPods release.
  if defined?(spm_dependency)
    spm_dependency(s,
      url: "https://github.com/Kommunicate-io/Kommunicate-iOS-SDK.git",
      requirement: { kind: "upToNextMajorVersion", minimumVersion: "7.3.8" },
      products: ["Kommunicate"]
    )
  else
    s.dependency 'Kommunicate', '7.3.6'
  end
end
