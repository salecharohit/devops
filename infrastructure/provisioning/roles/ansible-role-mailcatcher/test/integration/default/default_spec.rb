title 'Integration tests for mailcatcher ansible playbook'

listening_host = attribute('mailcatcher_listening_host', default: '0.0.0.0')
listening_ports = %w(1025 1080)

control 'mailcatcher-01' do
  impact 1.0
  title 'Verify mailcatcher service'

  describe processes('mailcatcher') do
    its('list.length') { should eq 1 }
  end

  listening_ports.each do |mc_port|
    describe port(mc_port) do
      it { should be_listening }
      its('addresses') { should include listening_host }
    end
  end
end
