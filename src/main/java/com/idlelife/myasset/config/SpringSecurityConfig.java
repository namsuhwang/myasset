package com.idlelife.myasset.config;

import com.idlelife.myasset.common.auth.AuthProvider;
import com.idlelife.myasset.common.auth.point.CustomAccessDeniedPoint;
import com.idlelife.myasset.common.auth.point.CustomAuthenticationEntryPoint;
import com.idlelife.myasset.config.filter.CustomFilter;
import com.idlelife.myasset.repository.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

//    private final AuthProvider authProvider;

//    @Autowired
//    AuthMapper authMapper;
//
//    @Value("${jwt.secret.signature}")
//    private String signatureKey;


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setMaxAge((long) 3600);
        configuration.setAllowCredentials(false);
        configuration.addExposedHeader("accessToken");
        configuration.addExposedHeader("content-disposition");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers(
                "/static/css/**",
                "/static/js/**",
                "/static/img/**",
                "/static/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final CustomFilter customFilter = new CustomFilter();
        http
            .httpBasic().disable()
                .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .antMatchers("/myasset/common/**").permitAll()		// 공통코드
                    .antMatchers("/myasset/auth/**").permitAll() 		// 로그인
                    .antMatchers("/myasset/member/reg/**").permitAll()  // 회원가입
                    .antMatchers("/exception/**").permitAll() 	        // 예외처리 포인트
                    .anyRequest().hasRole("MEMBER")				                    // 이외 나머지는 MEMBER 권한필요
                .and()
            .cors()
                .and()
            .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedPoint())
                .and()
            .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
            .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}