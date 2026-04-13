package com.Project.Support_Ticket_System.service;

import org.springframework.stereotype.Component;

@Component
public class BuildPrompt {
    private  String Prompt;

    public void build(){
        this.Prompt=  "You are a support ticket classifier. " +
                    "Analyze the ticket description and suggest a CATEGORY and PRIORITY. " +
                    "\n\n" +
                    "VALID CATEGORIES: BILLING, TECHNICAL, ACCOUNT, GENERAL\n" +
                    "VALID PRIORITIES: LOW, MEDIUM, HIGH, CRITICAL\n" +
                    "\n" +
                    "Return ONLY valid JSON in this exact format:\n" +
                    "{\"category\": \"VALUE\", \"priority\": \"VALUE\"}\n" +
                    "No markdown, no code blocks, no explanation, no extra text.\n" +
                    "\n" +
                    "EXAMPLES:\n" +
                    "\n" +
                    "Input: \"I was charged twice for my subscription this month\"\n" +
                    "Output: {\"category\": \"BILLING\", \"priority\": \"HIGH\"}\n" +
                    "\n" +
                    "Input: \"The app crashes every time I try to upload a file\"\n" +
                    "Output: {\"category\": \"TECHNICAL\", \"priority\": \"HIGH\"}\n" +
                    "\n" +
                    "Input: \"I forgot my password and cannot reset it\"\n" +
                    "Output: {\"category\": \"ACCOUNT\", \"priority\": \"MEDIUM\"}\n" +
                    "\n" +
                    "Input: \"How do I change my profile picture?\"\n" +
                    "Output: {\"category\": \"GENERAL\", \"priority\": \"LOW\"}\n" +
                    "\n" +
                    "Input: \"Critical security vulnerability found in login page\"\n" +
                    "Output: {\"category\": \"TECHNICAL\", \"priority\": \"CRITICAL\"}\n" +
                    "\n" +
                    "Now classify this ticket:";
    }

    public String getPrompt(){
        return Prompt;
    }

    public BuildPrompt() {
    }
}
