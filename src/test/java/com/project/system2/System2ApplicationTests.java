package com.project.system2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

//@SpringBootTest
class System2ApplicationTests {

//	["eat","tea","tan","ate","nat","bat"]
	public List<List<String>> groupAnagrams(String[] strs) {
		Map<String, List<String>> map = new HashMap<>();
		for(String str : strs){
			char[] charArray = str.toCharArray();
			Arrays.sort(charArray);
			map.computeIfAbsent(new String(charArray),k->new ArrayList<>()).add(str);
		}

		return new ArrayList<>(map.values());
	}



	@Test
	void contextLoads() {
		String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
		List<List<String>> result = groupAnagrams(strs);
		System.out.println(result);
	}

//	[100,4,200,1,3,2]
	public int longestConsecutive(int[] nums) {
		Set<Integer> set = new HashSet<>();
		for(int num : nums){
			set.add(num);
		}
		int longest = 0;
		for(int num : set){
			if(!set.contains(num-1)){
				int currentNum = num;
				int currentStreak = 1;
				while(set.contains(currentNum+1)){
					currentNum += 1;
					currentStreak += 1;
				}
				longest = Math.max(longest, currentStreak);
			}
		}
		return longest;
	}


	@Test
	void longestConsecutiveTest() {
		int[] nums = {-6,8,-5,7,-9,-1,-7,-6,-9,-7,5,7,-1,-8,-8,-2,0};
		int result = longestConsecutive(nums);
		System.out.println(result);
	}


	public int[]  moveZeroes(int[] nums) {
		int i0 = 0;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != 0 ){
				int num = nums[i];
				nums[i] = nums[i0];
				nums[i0] = num;
				i0++;
			}
		}
		return nums;
	}

	@Test
	public void test1(){
		int[] nums = {0,1,0,3,12};
		int[] result = moveZeroes(nums);
		System.out.println(1);
	}

}
