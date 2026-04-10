<h1>Project COZY</h1>
<h3>COllaboration made eaZY</h3>

<h3>S3 Profile Image (Private + Presigned URL)</h3>
<p>Bucket: <code>cozy-project-profile-images</code></p>
<p>AWS CLI setup (required for the upload script):</p>
<ul>
  <li>Install AWS CLI: <code>https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html</code></li>
  <li>Configure credentials: <code>aws configure</code></li>
</ul>
<ol>
  <li>Keep <code>Block all public access</code> enabled for this bucket.</li>
  <li>Ensure the IAM user/role for the app has <code>s3:PutObject</code> and <code>s3:GetObject</code> permissions.</li>
  <li>Set environment variables:
    <code>AWS_ACCESS_KEY_ID</code>,
    <code>AWS_SECRET_ACCESS_KEY</code>,
    <code>AWS_REGION</code>,
    <code>AWS_S3_BUCKET</code>,
    <code>DEFAULT_PROFILE_IMAGE_KEY</code> (optional, defaults to <code>profile_images/Default_Profile.png</code>),
    and <code>S3_PRESIGN_EXP_MINUTES</code> (optional, defaults to <code>60</code>).
  </li>
  <li>Upload the default profile image:
    <code>scripts/upload-default-profile.sh</code>
    (requires AWS CLI configured and a local image file; default path is <code>./assets/Default_Profile.png</code>).
  </li>
</ol>

<h3>EC2 Build & Run</h3>
<ol>
  <li>Install Java 17 (example on Ubuntu): <code>sudo apt-get update && sudo apt-get install -y openjdk-17-jdk</code></li>
  <li>Set environment variables:
    <code>AWS_ACCESS_KEY_ID</code>,
    <code>AWS_SECRET_ACCESS_KEY</code>,
    <code>AWS_REGION</code>,
    <code>AWS_S3_BUCKET</code>,
    <code>JWT_SECRET</code>,
    <code>POSTGRES_URL</code>,
    <code>POSTGRES_USER</code>,
    <code>POSTGRES_PASSWORD</code>
  </li>
  <li>Build: <code>./gradlew clean bootJar</code></li>
  <li>Run: <code>nohup java -jar build/libs/collaboproject-be-0.0.1-SNAPSHOT.jar &gt; app.log 2&gt;&amp;1 &amp;</code></li>
</ol>
