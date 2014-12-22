(ns books.amazon.amazon-json-test
  (:require [clojure.test :refer :all]
            [books.amazon.amazon-client :refer :all]
            [amazon.amazon-json :refer :all]))

(def doc "<ItemLookupResponse xmlns=\"http://webservices.amazon.com/AWSECommerceService/2011-08-01\">
   <OperationRequest>
      <HTTPHeaders>
         <Header Name=\"UserAgent\" Value=\"http-kit/2.0\" />
      </HTTPHeaders>
      <RequestId>989f41bb-9454-4a6f-a41d-b31bca02fbc4</RequestId>
      <Arguments>
         <Argument Name=\"Condition\" Value=\"All\" />
         <Argument Name=\"Operation\" Value=\"ItemLookup\" />
         <Argument Name=\"Service\" Value=\"AWSECommerceService\" />
         <Argument Name=\"Offer\" Value=\"All\" />
         <Argument Name=\"Signature\" Value=\"EakjsM0xMuv4ylEjJrnyZuTvAg27cDR5+Khb7ER/D18=\" />
         <Argument Name=\"AssociateTag\" Value=\"fleetiphotobl-20\" />
         <Argument Name=\"Version\" Value=\"2011-08-01\" />
         <Argument Name=\"ItemId\" Value=\"3775727388\" />
         <Argument Name=\"IdType\" Value=\"ISBN\" />
         <Argument Name=\"AWSAccessKeyId\" Value=\"AKIAIUZ3CEXC5EF5RJUQ\" />
         <Argument Name=\"Timestamp\" Value=\"2014-12-21T20:07:28Z\" />
         <Argument Name=\"ResponseGroup\" Value=\"ItemAttributes,OfferSummary,Images\" />
         <Argument Name=\"SearchIndex\" Value=\"Books\" />
      </Arguments>
      <RequestProcessingTime>0.0377270000000000</RequestProcessingTime>
   </OperationRequest>
   <Items>
      <Request>
         <IsValid>True</IsValid>
         <ItemLookupRequest>
            <Condition>All</Condition>
            <IdType>ISBN</IdType>
            <ItemId>3775727388</ItemId>
            <ResponseGroup>ItemAttributes</ResponseGroup>
            <ResponseGroup>OfferSummary</ResponseGroup>
            <ResponseGroup>Images</ResponseGroup>
            <SearchIndex>Books</SearchIndex>
            <VariationPage>All</VariationPage>
         </ItemLookupRequest>
      </Request>
      <Item>
         <ASIN>3775727388</ASIN>
         <DetailPageURL>http://www.amazon.co.uk/Walter-Niedermayr-Recollection/dp/3775727388%3FSubscriptionId%3DAKIAIUZ3CEXC5EF5RJUQ%26tag%3Dfleetiphotobl-20%26linkCode%3Dxm2%26camp%3D2025%26creative%3D165953%26creativeASIN%3D3775727388</DetailPageURL>
         <ItemLinks>
            <ItemLink>
               <Description>Add To Wishlist</Description>
               <URL>http://www.amazon.co.uk/gp/registry/wishlist/add-item.html%3Fasin.0%3D3775727388%26SubscriptionId%3DAKIAIUZ3CEXC5EF5RJUQ%26tag%3Dfleetiphotobl-20%26linkCode%3Dxm2%26camp%3D2025%26creative%3D12734%26creativeASIN%3D3775727388</URL>
            </ItemLink>
            <ItemLink>
               <Description>Tell A Friend</Description>
               <URL>http://www.amazon.co.uk/gp/pdp/taf/3775727388%3FSubscriptionId%3DAKIAIUZ3CEXC5EF5RJUQ%26tag%3Dfleetiphotobl-20%26linkCode%3Dxm2%26camp%3D2025%26creative%3D12734%26creativeASIN%3D3775727388</URL>
            </ItemLink>
            <ItemLink>
               <Description>All Customer Reviews</Description>
               <URL>http://www.amazon.co.uk/review/product/3775727388%3FSubscriptionId%3DAKIAIUZ3CEXC5EF5RJUQ%26tag%3Dfleetiphotobl-20%26linkCode%3Dxm2%26camp%3D2025%26creative%3D12734%26creativeASIN%3D3775727388</URL>
            </ItemLink>
            <ItemLink>
               <Description>All Offers</Description>
               <URL>http://www.amazon.co.uk/gp/offer-listing/3775727388%3FSubscriptionId%3DAKIAIUZ3CEXC5EF5RJUQ%26tag%3Dfleetiphotobl-20%26linkCode%3Dxm2%26camp%3D2025%26creative%3D12734%26creativeASIN%3D3775727388</URL>
            </ItemLink>
         </ItemLinks>
         <SmallImage>
            <URL>http://ecx.images-amazon.com/images/I/41rEba2X%2BwL._SL75_.jpg</URL>
            <Height Units=\"pixels\">75</Height>
            <Width Units=\"pixels\">64</Width>
         </SmallImage>
         <MediumImage>
            <URL>http://ecx.images-amazon.com/images/I/41rEba2X%2BwL._SL160_.jpg</URL>
            <Height Units=\"pixels\">160</Height>
            <Width Units=\"pixels\">136</Width>
         </MediumImage>
         <LargeImage>
            <URL>http://ecx.images-amazon.com/images/I/41rEba2X%2BwL.jpg</URL>
            <Height Units=\"pixels\">500</Height>
            <Width Units=\"pixels\">425</Width>
         </LargeImage>
         <ImageSets>
            <ImageSet Category=\"primary\">
               <SwatchImage>
                  <URL>http://ecx.images-amazon.com/images/I/41rEba2X%2BwL._SL30_.jpg</URL>
                  <Height Units=\"pixels\">30</Height>
                  <Width Units=\"pixels\">26</Width>
               </SwatchImage>
               <SmallImage>
                  <URL>http://ecx.images-amazon.com/images/I/41rEba2X%2BwL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">64</Width>
               </SmallImage>
               <ThumbnailImage>
                  <URL>http://ecx.images-amazon.com/images/I/41rEba2X%2BwL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">64</Width>
               </ThumbnailImage>
               <TinyImage>
                  <URL>http://ecx.images-amazon.com/images/I/41rEba2X%2BwL._SL110_.jpg</URL>
                  <Height Units=\"pixels\">110</Height>
                  <Width Units=\"pixels\">94</Width>
               </TinyImage>
               <MediumImage>
                  <URL>http://ecx.images-amazon.com/images/I/41rEba2X%2BwL._SL160_.jpg</URL>
                  <Height Units=\"pixels\">160</Height>
                  <Width Units=\"pixels\">136</Width>
               </MediumImage>
               <LargeImage>
                  <URL>http://ecx.images-amazon.com/images/I/41rEba2X%2BwL.jpg</URL>
                  <Height Units=\"pixels\">500</Height>
                  <Width Units=\"pixels\">425</Width>
               </LargeImage>
            </ImageSet>
            <ImageSet Category=\"variant\">
               <SwatchImage>
                  <URL>http://ecx.images-amazon.com/images/I/31wjYKb1hpL._SL30_.jpg</URL>
                  <Height Units=\"pixels\">30</Height>
                  <Width Units=\"pixels\">25</Width>
               </SwatchImage>
               <SmallImage>
                  <URL>http://ecx.images-amazon.com/images/I/31wjYKb1hpL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </SmallImage>
               <ThumbnailImage>
                  <URL>http://ecx.images-amazon.com/images/I/31wjYKb1hpL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </ThumbnailImage>
               <TinyImage>
                  <URL>http://ecx.images-amazon.com/images/I/31wjYKb1hpL._SL110_.jpg</URL>
                  <Height Units=\"pixels\">110</Height>
                  <Width Units=\"pixels\">92</Width>
               </TinyImage>
               <MediumImage>
                  <URL>http://ecx.images-amazon.com/images/I/31wjYKb1hpL._SL160_.jpg</URL>
                  <Height Units=\"pixels\">160</Height>
                  <Width Units=\"pixels\">133</Width>
               </MediumImage>
               <LargeImage>
                  <URL>http://ecx.images-amazon.com/images/I/31wjYKb1hpL.jpg</URL>
                  <Height Units=\"pixels\">500</Height>
                  <Width Units=\"pixels\">416</Width>
               </LargeImage>
            </ImageSet>
            <ImageSet Category=\"variant\">
               <SwatchImage>
                  <URL>http://ecx.images-amazon.com/images/I/41GMbVSDXYL._SL30_.jpg</URL>
                  <Height Units=\"pixels\">30</Height>
                  <Width Units=\"pixels\">25</Width>
               </SwatchImage>
               <SmallImage>
                  <URL>http://ecx.images-amazon.com/images/I/41GMbVSDXYL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </SmallImage>
               <ThumbnailImage>
                  <URL>http://ecx.images-amazon.com/images/I/41GMbVSDXYL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </ThumbnailImage>
               <TinyImage>
                  <URL>http://ecx.images-amazon.com/images/I/41GMbVSDXYL._SL110_.jpg</URL>
                  <Height Units=\"pixels\">110</Height>
                  <Width Units=\"pixels\">92</Width>
               </TinyImage>
               <MediumImage>
                  <URL>http://ecx.images-amazon.com/images/I/41GMbVSDXYL._SL160_.jpg</URL>
                  <Height Units=\"pixels\">160</Height>
                  <Width Units=\"pixels\">133</Width>
               </MediumImage>
               <LargeImage>
                  <URL>http://ecx.images-amazon.com/images/I/41GMbVSDXYL.jpg</URL>
                  <Height Units=\"pixels\">500</Height>
                  <Width Units=\"pixels\">416</Width>
               </LargeImage>
            </ImageSet>
            <ImageSet Category=\"variant\">
               <SwatchImage>
                  <URL>http://ecx.images-amazon.com/images/I/31mi0AWGwJL._SL30_.jpg</URL>
                  <Height Units=\"pixels\">30</Height>
                  <Width Units=\"pixels\">25</Width>
               </SwatchImage>
               <SmallImage>
                  <URL>http://ecx.images-amazon.com/images/I/31mi0AWGwJL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </SmallImage>
               <ThumbnailImage>
                  <URL>http://ecx.images-amazon.com/images/I/31mi0AWGwJL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </ThumbnailImage>
               <TinyImage>
                  <URL>http://ecx.images-amazon.com/images/I/31mi0AWGwJL._SL110_.jpg</URL>
                  <Height Units=\"pixels\">110</Height>
                  <Width Units=\"pixels\">92</Width>
               </TinyImage>
               <MediumImage>
                  <URL>http://ecx.images-amazon.com/images/I/31mi0AWGwJL._SL160_.jpg</URL>
                  <Height Units=\"pixels\">160</Height>
                  <Width Units=\"pixels\">133</Width>
               </MediumImage>
               <LargeImage>
                  <URL>http://ecx.images-amazon.com/images/I/31mi0AWGwJL.jpg</URL>
                  <Height Units=\"pixels\">500</Height>
                  <Width Units=\"pixels\">416</Width>
               </LargeImage>
            </ImageSet>
            <ImageSet Category=\"variant\">
               <SwatchImage>
                  <URL>http://ecx.images-amazon.com/images/I/51-dP2LprwL._SL30_.jpg</URL>
                  <Height Units=\"pixels\">30</Height>
                  <Width Units=\"pixels\">25</Width>
               </SwatchImage>
               <SmallImage>
                  <URL>http://ecx.images-amazon.com/images/I/51-dP2LprwL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </SmallImage>
               <ThumbnailImage>
                  <URL>http://ecx.images-amazon.com/images/I/51-dP2LprwL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </ThumbnailImage>
               <TinyImage>
                  <URL>http://ecx.images-amazon.com/images/I/51-dP2LprwL._SL110_.jpg</URL>
                  <Height Units=\"pixels\">110</Height>
                  <Width Units=\"pixels\">92</Width>
               </TinyImage>
               <MediumImage>
                  <URL>http://ecx.images-amazon.com/images/I/51-dP2LprwL._SL160_.jpg</URL>
                  <Height Units=\"pixels\">160</Height>
                  <Width Units=\"pixels\">133</Width>
               </MediumImage>
               <LargeImage>
                  <URL>http://ecx.images-amazon.com/images/I/51-dP2LprwL.jpg</URL>
                  <Height Units=\"pixels\">500</Height>
                  <Width Units=\"pixels\">416</Width>
               </LargeImage>
            </ImageSet>
            <ImageSet Category=\"variant\">
               <SwatchImage>
                  <URL>http://ecx.images-amazon.com/images/I/51I1KMBVycL._SL30_.jpg</URL>
                  <Height Units=\"pixels\">30</Height>
                  <Width Units=\"pixels\">25</Width>
               </SwatchImage>
               <SmallImage>
                  <URL>http://ecx.images-amazon.com/images/I/51I1KMBVycL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </SmallImage>
               <ThumbnailImage>
                  <URL>http://ecx.images-amazon.com/images/I/51I1KMBVycL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </ThumbnailImage>
               <TinyImage>
                  <URL>http://ecx.images-amazon.com/images/I/51I1KMBVycL._SL110_.jpg</URL>
                  <Height Units=\"pixels\">110</Height>
                  <Width Units=\"pixels\">92</Width>
               </TinyImage>
               <MediumImage>
                  <URL>http://ecx.images-amazon.com/images/I/51I1KMBVycL._SL160_.jpg</URL>
                  <Height Units=\"pixels\">160</Height>
                  <Width Units=\"pixels\">133</Width>
               </MediumImage>
               <LargeImage>
                  <URL>http://ecx.images-amazon.com/images/I/51I1KMBVycL.jpg</URL>
                  <Height Units=\"pixels\">500</Height>
                  <Width Units=\"pixels\">416</Width>
               </LargeImage>
            </ImageSet>
            <ImageSet Category=\"variant\">
               <SwatchImage>
                  <URL>http://ecx.images-amazon.com/images/I/414voCGTE5L._SL30_.jpg</URL>
                  <Height Units=\"pixels\">30</Height>
                  <Width Units=\"pixels\">25</Width>
               </SwatchImage>
               <SmallImage>
                  <URL>http://ecx.images-amazon.com/images/I/414voCGTE5L._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </SmallImage>
               <ThumbnailImage>
                  <URL>http://ecx.images-amazon.com/images/I/414voCGTE5L._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </ThumbnailImage>
               <TinyImage>
                  <URL>http://ecx.images-amazon.com/images/I/414voCGTE5L._SL110_.jpg</URL>
                  <Height Units=\"pixels\">110</Height>
                  <Width Units=\"pixels\">92</Width>
               </TinyImage>
               <MediumImage>
                  <URL>http://ecx.images-amazon.com/images/I/414voCGTE5L._SL160_.jpg</URL>
                  <Height Units=\"pixels\">160</Height>
                  <Width Units=\"pixels\">133</Width>
               </MediumImage>
               <LargeImage>
                  <URL>http://ecx.images-amazon.com/images/I/414voCGTE5L.jpg</URL>
                  <Height Units=\"pixels\">500</Height>
                  <Width Units=\"pixels\">416</Width>
               </LargeImage>
            </ImageSet>
            <ImageSet Category=\"variant\">
               <SwatchImage>
                  <URL>http://ecx.images-amazon.com/images/I/41tvzuG4v7L._SL30_.jpg</URL>
                  <Height Units=\"pixels\">30</Height>
                  <Width Units=\"pixels\">25</Width>
               </SwatchImage>
               <SmallImage>
                  <URL>http://ecx.images-amazon.com/images/I/41tvzuG4v7L._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </SmallImage>
               <ThumbnailImage>
                  <URL>http://ecx.images-amazon.com/images/I/41tvzuG4v7L._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">62</Width>
               </ThumbnailImage>
               <TinyImage>
                  <URL>http://ecx.images-amazon.com/images/I/41tvzuG4v7L._SL110_.jpg</URL>
                  <Height Units=\"pixels\">110</Height>
                  <Width Units=\"pixels\">92</Width>
               </TinyImage>
               <MediumImage>
                  <URL>http://ecx.images-amazon.com/images/I/41tvzuG4v7L._SL160_.jpg</URL>
                  <Height Units=\"pixels\">160</Height>
                  <Width Units=\"pixels\">133</Width>
               </MediumImage>
               <LargeImage>
                  <URL>http://ecx.images-amazon.com/images/I/41tvzuG4v7L.jpg</URL>
                  <Height Units=\"pixels\">500</Height>
                  <Width Units=\"pixels\">416</Width>
               </LargeImage>
            </ImageSet>
            <ImageSet Category=\"variant\">
               <SwatchImage>
                  <URL>http://ecx.images-amazon.com/images/I/411Ntk9SuYL._SL30_.jpg</URL>
                  <Height Units=\"pixels\">30</Height>
                  <Width Units=\"pixels\">25</Width>
               </SwatchImage>
               <SmallImage>
                  <URL>http://ecx.images-amazon.com/images/I/411Ntk9SuYL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">63</Width>
               </SmallImage>
               <ThumbnailImage>
                  <URL>http://ecx.images-amazon.com/images/I/411Ntk9SuYL._SL75_.jpg</URL>
                  <Height Units=\"pixels\">75</Height>
                  <Width Units=\"pixels\">63</Width>
               </ThumbnailImage>
               <TinyImage>
                  <URL>http://ecx.images-amazon.com/images/I/411Ntk9SuYL._SL110_.jpg</URL>
                  <Height Units=\"pixels\">110</Height>
                  <Width Units=\"pixels\">92</Width>
               </TinyImage>
               <MediumImage>
                  <URL>http://ecx.images-amazon.com/images/I/411Ntk9SuYL._SL160_.jpg</URL>
                  <Height Units=\"pixels\">160</Height>
                  <Width Units=\"pixels\">133</Width>
               </MediumImage>
               <LargeImage>
                  <URL>http://ecx.images-amazon.com/images/I/411Ntk9SuYL.jpg</URL>
                  <Height Units=\"pixels\">500</Height>
                  <Width Units=\"pixels\">417</Width>
               </LargeImage>
            </ImageSet>
         </ImageSets>
         <ItemAttributes>
            <Author>Walter Niedermayr</Author>
            <Binding>Hardcover</Binding>
            <EAN>9783775727389</EAN>
            <EANList>
               <EANListElement>9783775727389</EANListElement>
            </EANList>
            <Edition>Bilingual</Edition>
            <Format>Illustrated</Format>
            <ISBN>3775727388</ISBN>
            <ItemDimensions>
               <Height Units=\"hundredths-inches\">1102</Height>
               <Length Units=\"hundredths-inches\">1339</Length>
               <Weight Units=\"hundredths-pounds\">321</Weight>
               <Width Units=\"hundredths-inches\">71</Width>
            </ItemDimensions>
            <Label>Hatje Cantz</Label>
            <Languages>
               <Language>
                  <Name>English</Name>
                  <Type>Unknown</Type>
               </Language>
               <Language>
                  <Name>German</Name>
                  <Type>Unknown</Type>
               </Language>
               <Language>
                  <Name>German</Name>
                  <Type>Original Language</Type>
               </Language>
               <Language>
                  <Name>English</Name>
                  <Type>Original Language</Type>
               </Language>
               <Language>
                  <Name>English</Name>
                  <Type>Published</Type>
               </Language>
            </Languages>
            <ListPrice>
               <Amount>5500</Amount>
               <CurrencyCode>GBP</CurrencyCode>
               <FormattedPrice>£55.00</FormattedPrice>
            </ListPrice>
            <Manufacturer>Hatje Cantz</Manufacturer>
            <NumberOfItems>1</NumberOfItems>
            <NumberOfPages>144</NumberOfPages>
            <PackageDimensions>
               <Height Units=\"hundredths-inches\">79</Height>
               <Length Units=\"hundredths-inches\">1173</Length>
               <Weight Units=\"hundredths-pounds\">322</Weight>
               <Width Units=\"hundredths-inches\">1008</Width>
            </PackageDimensions>
            <ProductGroup>Book</ProductGroup>
            <ProductTypeName>ABIS_BOOK</ProductTypeName>
            <PublicationDate>2010-09-20</PublicationDate>
            <Publisher>Hatje Cantz</Publisher>
            <Studio>Hatje Cantz</Studio>
            <Title>Walter Niedermayr: Recollection</Title>
         </ItemAttributes>
         <OfferSummary>
            <LowestNewPrice>
               <Amount>1800</Amount>
               <CurrencyCode>GBP</CurrencyCode>
               <FormattedPrice>£18.00</FormattedPrice>
            </LowestNewPrice>
            <LowestUsedPrice>
               <Amount>1334</Amount>
               <CurrencyCode>GBP</CurrencyCode>
               <FormattedPrice>£13.34</FormattedPrice>
            </LowestUsedPrice>
            <TotalNew>19</TotalNew>
            <TotalUsed>8</TotalUsed>
            <TotalCollectible>0</TotalCollectible>
            <TotalRefurbished>0</TotalRefurbished>
         </OfferSummary>
      </Item>
   </Items>
</ItemLookupResponse>")