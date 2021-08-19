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
  s.author             = { "author" => "ashish@kommunicate.io" }
  s.platform     = :ios, "10.0"
  s.source       = { :git => "https://github.com/Kommunicate-io/Kommunicate-React-Native-SDK", :tag => "master" }
  s.source_files  = "ios/*.{h,m,swift}"
  s.requires_arc = true

  s.dependency 'React'
  s.dependency 'Kommunicate', '~> 6.2.1'
end
