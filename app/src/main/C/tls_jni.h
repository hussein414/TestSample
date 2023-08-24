#ifndef VPN_TLS_JNI_H
#define VPN_TLS_JNI_H
extern void init_tls_tunnel_lib();
extern void tls_tunnel_stop();
extern void tls_tunnel_start(char* uuid,char *config_name);
extern void setTunneFD(int fd);
extern char *getTunnelParams();
#endif //VPN_TLS_JNI_H
