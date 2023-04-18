$file_content = Get-Content ".\.env"
$file_content = $file_content -join [Environment]::NewLine

$configuration = ConvertFrom-StringData($file_content)

foreach ($entry in $configuration.GetEnumerator()) {
	[System.Environment]::SetEnvironmentVariable($entry.Name, $entry.Value, 'Process')
}

jabba use adopt@1.8.0-292;

#Start-Job -Name interceptorJob -ScriptBlock {cd "D:/workspace/myProjects/java/play/interceptor"; jabba use adopt@1.8.0-292; sh start.sh};
sh start.sh