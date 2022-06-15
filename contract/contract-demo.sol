// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721Enumerable.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/utils/Address.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/utils/math/SafeMath.sol";

contract Demo is ERC721, ERC721Enumerable, Ownable {
    using Counters for Counters.Counter;
    using SafeMath for uint256;

    uint256 public constant MAX_MINTS_PER_TXN = 100 ;

    Counters.Counter private _tokenIdCounter;

    uint256 public maxTokenSupply;

    string public baseURI;

    mapping (address => uint256) public amount;
    
    constructor(string memory name, string memory symbol, uint256 maxBigBenTokenSupply) ERC721(name, symbol) {
        maxTokenSupply = maxBigBenTokenSupply;
    }



    /*
    * Mint NFTs
    */
    function mint(uint256 numberOfTokens) public onlyOwner {
        require ( numberOfTokens <= MAX_MINTS_PER_TXN, "You can only adopt 20 at a time" ) ; 
        require(totalSupply ( ).add(numberOfTokens)<= maxTokenSupply, "canot over maxTokenSupply");
       
        for(uint256 i = 0; i < numberOfTokens; i++) {
            uint256 mintIndex = _tokenIdCounter.current() + 1;
            if (mintIndex <= maxTokenSupply) {
                _safeMint(msg.sender, mintIndex);
                _tokenIdCounter.increment();
            }
        }
    }

    

    function _baseURI() internal view virtual override returns (string memory) {
        return baseURI;
    }

    function setBaseURI(string memory newBaseURI) public onlyOwner {
        baseURI = newBaseURI;
    }

    function _beforeTokenTransfer(address from, address to, uint256 tokenId) internal override(ERC721, ERC721Enumerable) onlyOwner{
        super._beforeTokenTransfer(from, to, tokenId);
    }

    function supportsInterface(bytes4 interfaceId) public view override(ERC721, ERC721Enumerable) onlyOwner returns (bool) {
        return super.supportsInterface(interfaceId);
    }

}