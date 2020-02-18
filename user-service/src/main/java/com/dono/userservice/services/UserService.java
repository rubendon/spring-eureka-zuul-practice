package com.dono.userservice.services;

import com.dono.userservice.dtos.UserDetailsDTO;
import com.dono.userservice.entities.UserDetails;
import com.dono.userservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService
{
    private final Logger LOGGER = LoggerFactory.getLogger( UserService.class );

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserDetailsDTO saveUpdate( UserDetailsDTO inputUser )
    {
        try
        {
            UserDetails userDetails = new UserDetails();
            userDetails.setName( inputUser.getName() );
            userDetails.setAge( Integer.parseInt( inputUser.getAge() ) );
            return getUserDetailsDTO( userRepository.save( userDetails ) );
        }
        catch ( Exception e )
        {
            LOGGER.warn( "Exception in UserService -> SaveUpdate(): " + e );
        }
        return null;
    }

    public UserDetailsDTO getById( Long id )
    {
        return getUserDetailsDTO( userRepository.getOne( id ) );
    }

    public List<UserDetailsDTO> getByName( String name )
    {
        List<UserDetails> userDetailsList = userRepository.findUserByName( name );
        if ( CollectionUtils.isEmpty( userDetailsList ) )
            return null;
        return userDetailsList
                .stream()
                .map( this::getUserDetailsDTO )
                .collect( Collectors.toList() );
    }

    public UserDetailsDTO getUserDetailsDTO( UserDetails userDetails )
    {
        return new UserDetailsDTO(
                userDetails.getId().toString(),
                userDetails.getName(),
                userDetails.getAge().toString()
        );
    }
}
