const artistDisplay = document.querySelector('.artist-display');


document.querySelector('.add-artist-button').addEventListener('click', addArtist);

// function addArtist() {
//     let artistName = createArtistName();
//     console.log(artistName);
//     let artist = {
//         "name": artistName,
//         "userId": userId
//     }
//     fetch("/users/" + userId + "/create", {
//         method: 'POST',
//                                             headers: {
//                                                 'Content-Type': 'application/json'
//                                             },
//                                             body: JSON.stringify(artist),
//                                             responseType: 'json'
//                                             })
//                                              .then((response) => {
//                                                     if (!response.ok) {
//                                                         throw new Error('Network response was not ok');
//                                                     }
//                                                     return response.json();
//                                                 })
//                                                 .then ((data) => {
//                                                         console.log(data)
//                                                         document.querySelector('.add-artist-button').value = ''
//                                                 })
//                                                 .catch((error) => {
//                                                     console.error('Error:', error)
//                                                 })
//                                                 .finally (() => {
//                                                             console.log("new artist added successfully")
//                                                 })
//     location.reload()
//     }



function createArtistName(){
  var prefixes = ['Flaming', 'Black', 'Talking', 'Broken', 'Ashen', 'Rainbow', 'Wandering', 'Lost', 'Breathing', 'Rough', 'Rolling', 'Thundering', 'Hipster', 'Punk', 'Goth', 'White', 'Pale', 'Lunar', 'Mystic', 'Screaming', 'Sexy','Diabolical', 'Evil', 'Thumping', 'Ascending', 'Falling', 'Spinning', 'Drooling', 'Secret', 'Mad', 'Hot', 'Veiled', 'Hidden', 'Psychic', 'Nightly', 'Eerie', 'Transparent', 'Wild', 'Smashing', 'Chunking', 'Guns and', 'Roamin\' ', 'Stylish', 'Creepy', 'Nerdy', 'Anti','Panoramic', 'McShizzle', 'Pensive', 'Crushing', 'Dead Man\'s', 'Lords of', 'Burnt', 'Wheeled', 'Living', 'Azure', 'Undead', 'Exploding', 'Spiralling', 'Boom-boom', 'Serious', 'Stoic', 'Deep', 'Somber', 'Squirming', 'Vanilla', 'Screeching', 'Thrashing', 'Selective', 'Swift', 'Soaring', 'Mighty'];
  var suffixes = ['Flames', 'Banisters', 'Skulls', 'Unicorns', 'Souls', 'Corpses', 'Flannel', 'Zombies', 'Lampshades', 'Scientists', 'Ghosts', 'Dude and His Merry Gang of Band Nerds', 'Hunters', 'Sirens', 'Lozenges', 'Stones', 'Heads', 'Hands', 'Cerulean', 'Lightning', 'Blades', 'Gang', 'Homeboys', 'Critics', 'Emos', 'Rebels','Pirates', 'Pumpkins', 'Roses', 'Demons', 'Tractors', 'Men', 'Gals', 'Riders', 'Ray-Bans', 'Trenchcoats', 'Creepers', 'Gravity', 'Diamonds', 'Mirrors', 'Jefes', 'Esperantos', 'Crimson', 'Wrappers', ' José y los gauchos' , 'Heat', 'Fever', 'Wave', 'Spell', 'Spectacle', 'Voices', 'Group', 'Fliers', 'Homies', 'Rum', 'Wheels', 'Brits', 'Machines', 'Assassination', 'Flint', 'Noises', 'Perspiration', 'Perpetrator', 'Betrayed', 'Wasslers', 'Whirlpool', 'Pistols', 'Boulders', 'Trinkets', 'Rag Dolls', 'Bazookas', 'Popsicle', 'Ice Cubes', 'Banshees', 'Frost', 'Darkness', 'Beat', 'Freeze', 'Kittens', 'Superheroes', 'Bagel', 'Flaxseeds', 'Children', 'Love', 'Equinox', 'Life'];

  var randomPrefix = prefixes[Math.floor(Math.random() * prefixes.length)]
  var randomSuffix = suffixes[Math.floor(Math.random() * suffixes.length)]

  return randomPrefix + " " + randomSuffix


  }




