/*
 * Copyright 2018 gavin17.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lq;

/**
 *
 * @author gavin17
 */
public class Sword
{
   long thrust(long strength)
   {
       return strength;
   }
   long slash(long strength)
   {
       return strength;
   }
   long special(long strength, long sw_special)
   {
       return strength + sw_special;
   }
}

class mortifer extends Sword
{
    @Override
   long slash(long strength)
   {
       return strength + 12;
   }
   @Override
   long thrust(long strength)
   {
       return strength + 12;
   }
   @Override
   long special(long strength, long sw_special)
   {
       return strength + sw_special;
   }
}