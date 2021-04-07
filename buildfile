repositories.remote << 'http://repo1.maven.org/maven2'
# Version number for this release
VERSION_NUMBER = '0.0.1'

desc 'Utility classes'
define 'util' do
    project.group = 'com.gothpoodle.util'
    project.version = VERSION_NUMBER
    compile.with Dir[_("lib/*.jar")]
    compile.options.lint = 'all'
    package(:jar, :id => 'gutil')
end

