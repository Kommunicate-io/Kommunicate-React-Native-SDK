
Pod::Spec.new do |s|
  s.name         = "RNKommunicateChat"
  s.version      = "1.0.0"
  s.summary      = "RNKommunicateChat"
  s.description  = <<-DESC
                  RNKommunicateChat
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "ashish@kommunicate.io" }
  s.platform     = :ios, "10.0"
  s.source       = { :git => "https://github.com/author/RNKommunicateChat.git", :tag => "master" }
  s.source_files  = "RNKommunicateChat/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  s.dependency 'Kommunicate', '~> 2.0.0'
end

  