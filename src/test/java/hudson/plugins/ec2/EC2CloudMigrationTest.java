package hudson.plugins.ec2;

import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import jenkins.model.Jenkins;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.LocalData;

import java.util.Optional;

import static org.junit.Assert.*;

public class EC2CloudMigrationTest {

    @Rule
    public JenkinsRule r = new JenkinsRule();

    @Test
    @LocalData
    public void testPrivateKeyMigrationToSshCredentials() {
        assertEquals(4, r.jenkins.getNumExecutors());
        assertEquals(1, r.jenkins.clouds.size());
        EC2Cloud cloud = (EC2Cloud) Jenkins.get().getCloud("ec2-myEc2Cloud");

        String credsId = cloud.getSshKeysCredentialsId();
        assertNotNull(credsId);

        Optional<BasicSSHUserPrivateKey> keyCredential = SystemCredentialsProvider.getInstance().getCredentials()
                .stream()
                .filter((cred) -> cred instanceof BasicSSHUserPrivateKey)
                .filter((cred) -> ((BasicSSHUserPrivateKey)cred).getPrivateKey().trim().equals("myPrivateKey"))
                    .map(cred -> (BasicSSHUserPrivateKey)cred)
                .findFirst();

        assertTrue(keyCredential.isPresent());
    }

}
