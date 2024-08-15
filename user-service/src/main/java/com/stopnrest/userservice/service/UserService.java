package com.stopnrest.userservice.service;

import com.stopnrest.userservice.dto.CreditCardDTO;
import com.stopnrest.userservice.dto.StayInfoDTO;
import com.stopnrest.userservice.dto.UserDTO;
import com.stopnrest.userservice.dto.UserPubDTO;
import com.stopnrest.userservice.model.CreditCard;
import com.stopnrest.userservice.model.StayInfo;
import com.stopnrest.userservice.model.User;
import com.stopnrest.userservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<String> createUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findById(userDTO.getUserName());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        if (userDTO.getRole() == null) {
            userDTO.setRole("PUBLIC");
        }
        if(userDTO.getCreditCards() == null){
            List<CreditCardDTO>card = new ArrayList<>();
            userDTO.setCreditCards(card);
        }
        if(userDTO.getStayHistory() == null){
            List<StayInfoDTO>stay = new ArrayList<>();
            userDTO.setStayHistory(stay);
        }
        User user = modelMapper.map(userDTO, User.class);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    public ResponseEntity<String> updateUser(String username, UserDTO userDTO) {
        if (!username.equals(userDTO.getUserName())) {
            return ResponseEntity.badRequest().body("Username cannot be changed");
        }

        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(userDTO.getFirstName());
            existingUser.setLastName(userDTO.getLastName());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setMobileNumber(userDTO.getMobileNumber());
            existingUser.setRole(userDTO.getRole());

            if (userDTO.getCreditCards() != null) {
                existingUser.setCreditCards(userDTO.getCreditCards().stream()
                        .map(card -> modelMapper.map(card, CreditCard.class))
                        .collect(Collectors.toList()));
            }

            if (userDTO.getStayHistory() != null) {
                existingUser.setStayHistory(userDTO.getStayHistory().stream()
                        .map(stay -> modelMapper.map(stay, StayInfo.class))
                        .collect(Collectors.toList()));
            }

            existingUser.setHotelIds(userDTO.getHotelIds());

            userRepository.save(existingUser);
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username " + username + " not found");
        }
    }

    public ResponseEntity<String> deleteUser(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username " + username + " not found");
        }
    }

    public ResponseEntity<?> getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getRole().equals("ADMIN") || user.getRole().equals("SUPER_ADMIN")) {
                UserDTO admin = modelMapper.map(user, UserDTO.class);
                return ResponseEntity.ok(admin);
            } else {
                user.setRole("PUBLIC");
                UserPubDTO userPubDTO = modelMapper.map(user, UserPubDTO.class);
                return ResponseEntity.ok(userPubDTO);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username " + username + " does not exist");
        }
    }

    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    public ResponseEntity<String> addCreditCard(String username, CreditCardDTO creditCardDTO) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            CreditCard creditCard = modelMapper.map(creditCardDTO, CreditCard.class);
            user.getCreditCards().add(creditCard);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Credit card added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username " + username + " not found");
        }
    }

    public ResponseEntity<String> removeCreditCard(String username, CreditCardDTO creditCardRemovalDTO) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean removed = user.getCreditCards().removeIf(card -> card.getCardNumber().equals(creditCardRemovalDTO.getCardNumber()));

            if (removed) {
                userRepository.save(user);
                return ResponseEntity.ok("Credit card removed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card with number " + creditCardRemovalDTO.getCardNumber() + " not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username " + username + " not found");
        }
    }

    public ResponseEntity<?> getAllCreditCards(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<CreditCardDTO> creditCardDTOs = user.getCreditCards().stream()
                    .map(card -> modelMapper.map(card, CreditCardDTO.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(creditCardDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Credit Card Found");
        }
    }

    public ResponseEntity<?> getAllStayInfo(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<StayInfoDTO> stayInfoDTOs = user.getStayHistory().stream()
                    .map(stay -> modelMapper.map(stay, StayInfoDTO.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(stayInfoDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username " + username + " not found");
        }
    }

    public ResponseEntity<String> updateUserStatus(Long bookingID, String userName) {
        Optional<User> userOptional = userRepository.findById(userName);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            StayInfo info = new StayInfo();
            info.setReservationID(bookingID);
            List<StayInfo> stayInfo = user.getStayHistory();
            if (stayInfo == null) {
                stayInfo = new ArrayList<>();
            }
            stayInfo.add(info);
            user.setStayHistory(stayInfo);
            userRepository.save(user);
            return ResponseEntity.ok("Stay information updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username " + userName + " not found");
        }
    }
}
